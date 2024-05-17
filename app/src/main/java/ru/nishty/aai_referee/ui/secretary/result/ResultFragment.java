package ru.nishty.aai_referee.ui.secretary.result;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ResultFragment extends Fragment {
    private View view;

    private static final String ARG_ID = "id";
    private static String ID;
    private static final String ARG_CATEGORY = "category";
    private static int CATEGORY;
    private View rootView;
    private int tours;
    private int currentPage = 0;


    private RecyclerView recyclerView;

    public ResultFragment() {
    }

    public static ResultFragment newInstance(String id, int category, int position) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString(ARG_ID, id);
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (getArguments() != null) {
            ID = getArguments().getString(ARG_ID);
            CATEGORY = getArguments().getInt(ARG_CATEGORY);
            currentPage = getArguments().getInt("position");
        }
        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        int conf = dataBaseHelperSecretary.getCategoryCongigById(db,CATEGORY);
        tours = dataBaseHelperSecretary.getCategoryTourById(db,CATEGORY);
        if (tours==3){
            rootView = inflater.inflate(R.layout.fragment_result, container, false);
            recyclerView = rootView.findViewById(R.id.result_list);
        } else if (tours==2) {
            rootView = inflater.inflate(R.layout.fragment_result_2, container, false);
            recyclerView = rootView.findViewById(R.id.result_list2);
        } else if (tours==1) {
            rootView = inflater.inflate(R.layout.fragment_result_1, container, false);
            recyclerView = rootView.findViewById(R.id.result_list1);
        }

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        List<ResultPlayer> resultPlayers = getResultPlayersFromDatabase();


        MyResultRecyclerViewAdapter adapter = new MyResultRecyclerViewAdapter(resultPlayers,tours);

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<ResultPlayer> getResultPlayersFromDatabase() {
        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        List<Player> players = dataBaseHelperSecretary.getPlayersCat(db, ID,CATEGORY);

        List<ResultPlayer> resultPlayers = new ArrayList<>();
        for (Player player : players) {
            int playerId = player.getId();
            int playerCategory = player.getCategoryId();
            List<Integer> protocolsId = dataBaseHelperSecretary.getProtocolsByPlayer(db, ID, playerId);
            List<Protocol> protocols = dataBaseHelperSecretary.getProtocolsById(db, ID, protocolsId);
            String name = player.getName();
            int range = player.getGrade();
            String region = dataBaseHelperSecretary.getRegionNameById(db,player.getRegionId());

            ResultPlayer resultPlayer = new ResultPlayer(ID, protocols, name, tours,range,region);
            resultPlayers.add(resultPlayer);
        }

        db.close();

        return resultPlayers;
    }
}

