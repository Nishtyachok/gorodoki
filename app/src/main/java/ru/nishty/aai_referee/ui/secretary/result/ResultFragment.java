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
import java.util.UUID;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ResultFragment extends Fragment {
    private View view;
    private static final String ARG_ID = "id";
    private static String ID;

    private RecyclerView recyclerView;

    public ResultFragment() {
    }

    public static ResultFragment newInstance(UUID id) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            ID = getArguments().getString(ARG_ID);
        }
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        // Устанавливаем ориентацию экрана на горизонтальную
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Получаем данные игроков из базы данных
        List<ResultPlayer> resultPlayers = getResultPlayersFromDatabase();

        // Находим RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.result_list);

        // Создаем адаптер и передаем ему список игроков
        MyResultRecyclerViewAdapter adapter = new MyResultRecyclerViewAdapter(resultPlayers);

        // Устанавливаем адаптер для RecyclerView
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<ResultPlayer> getResultPlayersFromDatabase() {
        // Получите доступ к базе данных
        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        // Получите список всех игроков для указанного соревнования
        List<Player> players = dataBaseHelperSecretary.getPlayers(db, ID);

        // Создайте список ResultPlayer и заполните его данными из базы данных
        List<ResultPlayer> resultPlayers = new ArrayList<>();
        for (Player player : players) {
            int playerId = player.getId();
            List<Integer> protocolsId = dataBaseHelperSecretary.getProtocolsByPlayer(db, ID, playerId);
            List<Protocol> protocols = dataBaseHelperSecretary.getProtocolsById(db, ID, protocolsId);
            String name = player.getName();
            int range = player.getGrade();
            String region = dataBaseHelperSecretary.getRegionNameById(db,player.getRegionId());

            ResultPlayer resultPlayer = new ResultPlayer(ID, protocols, name, range, region);
            resultPlayers.add(resultPlayer);
        }

        // Закройте базу данных
        db.close();

        return resultPlayers;
    }
}

