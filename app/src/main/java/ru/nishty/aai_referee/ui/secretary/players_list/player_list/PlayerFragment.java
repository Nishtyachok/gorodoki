package ru.nishty.aai_referee.ui.secretary.players_list.player_list;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.databinding.FragmentPlayerBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerFragment extends Fragment {

    private RecyclerView recyclerView;
    private FragmentPlayerBinding binding;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private String competitionUuid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            competitionUuid = getArguments().getString("competitionUuid");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        recyclerView = view.findViewById(R.id.rvPlayers);
        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(playerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(playerAdapter);

        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        loadPlayersFromDatabase();

        return view;
    }

    private void loadPlayersFromDatabase() {
        // Получение игроков из базы данных и добавление их в список
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        playerList.clear();
        playerList.addAll(dataBaseHelperSecretary.getPlayers(db, UUID.fromString(competitionUuid)));
        playerAdapter.notifyDataSetChanged();
    }
}



