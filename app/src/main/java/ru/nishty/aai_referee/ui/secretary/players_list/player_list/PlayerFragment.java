package ru.nishty.aai_referee.ui.secretary.players_list.player_list;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;
    private List<Player> playersList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private String competitionUuid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        recyclerView = view.findViewById(R.id.rvPlayers);
        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();


        // Получаем UUID соревнования из аргументов фрагмента
        Bundle arguments = getArguments();
        if (arguments != null) {
            competitionUuid = arguments.getString("competitionUuid");
        }
        playersList = dataBaseHelperSecretary.getPlayers(dbr, UUID.fromString(competitionUuid));
        if(playersList==null)
            playersList = new ArrayList<>();
        setupRecyclerView();
        FloatingActionButton fabAddPlayer = view.findViewById(R.id.fabAddPlayer);
        fabAddPlayer.setOnClickListener(v -> showAddPlayerDialog());

        return view;
    }

    private void setupRecyclerView() {
        playerAdapter = new PlayerAdapter(playersList);
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showAddPlayerDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_player, null);
        EditText etPlayerName = dialogView.findViewById(R.id.etPlayerName);
        Spinner spinnerGrade = dialogView.findViewById(R.id.spinnerGrade);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Spinner spinnerRegion = dialogView.findViewById(R.id.spinnerRegion);

        // Получаем список регионов
        List<Region> regions = dataBaseHelperSecretary.getRegions(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(competitionUuid));

        List<String> regionNames = new ArrayList<>();
        for (Region region : regions) {
            regionNames.add(region.getName());
        }

        List<Category> categories = dataBaseHelperSecretary.getCategories(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(competitionUuid));
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        // Создаем адаптер для Spinner'а с регионами
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, regionNames);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Устанавливаем адаптер для Spinner'а
        spinnerRegion.setAdapter(regionAdapter);
        spinnerCategory.setAdapter(categoryAdapter);

        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String name = etPlayerName.getText().toString();
                    int selectedGrade = Integer.parseInt(spinnerGrade.getSelectedItem().toString());
                    int selectedCategory = Integer.parseInt(spinnerCategory.getSelectedItem().toString());
                    int selectedRegion = Integer.parseInt(spinnerRegion.getSelectedItem().toString());

                    Player player = new Player();
                    player.setComp_id(UUID.fromString(competitionUuid));
                    player.setName(name);
                    player.setGrade(selectedGrade);
                    player.setCategoryId(selectedCategory);
                    player.setRegionId(selectedRegion);


                    SQLiteDatabase db = dataBaseHelperSecretary.getWritableDatabase();
                    long playerId = dataBaseHelperSecretary.addPlayer(db, player, UUID.fromString(competitionUuid));

                    if (playerId != -1) {
                        playersList.add(player);
                        playerAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

}
