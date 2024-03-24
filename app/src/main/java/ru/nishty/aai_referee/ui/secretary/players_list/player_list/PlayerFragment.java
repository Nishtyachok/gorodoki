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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Region;

public class PlayerFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;
    private List<Player> playersList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private DataBaseContractSecretary dataBaseContractSecretary;

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

        List<String> gradeNames = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            int gradeResourceId = DataBaseContractSecretary.GradeHelper.getGrade(i);
            if (gradeResourceId != -1) {
                gradeNames.add(getContext().getString(gradeResourceId));
            }
        }

        List<Region> regions = dataBaseHelperSecretary.getRegions(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(competitionUuid));
        List<String> regionNames = new ArrayList<>();
        List<Integer> regionIds = new ArrayList<>();

        for (Region region : regions) {
            regionNames.add(region.getName());
            regionIds.add(region.getId());
        }

        List<Category> categories = dataBaseHelperSecretary.getCategories(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(competitionUuid));
        List<String> categoryNames = new ArrayList<>();
        List<Integer> categoryIds = new ArrayList<>();

        for (Category category : categories) {
            categoryNames.add(category.getName());
            categoryIds.add(category.getId());
        }

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, gradeNames);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, regionNames);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Устанавливаем адаптер для Spinner'а
        spinnerGrade.setAdapter(gradeAdapter);
        spinnerGrade.setPrompt(getString(R.string.hint_grade));
        spinnerRegion.setAdapter(regionAdapter);
        spinnerGrade.setPrompt(getString(R.string.hint_region));
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerGrade.setPrompt(getString(R.string.hint_category));


        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String name = etPlayerName.getText().toString();
                    int selectedGrade = spinnerGrade.getSelectedItemPosition()+1;

                    int selectedPositionCategory = spinnerCategory.getSelectedItemPosition();
                    int selectedCategory = categoryIds.get(selectedPositionCategory);

                    int selectedPositionRegion = spinnerRegion.getSelectedItemPosition();
                    int selectedRegion = regionIds.get(selectedPositionRegion);

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
