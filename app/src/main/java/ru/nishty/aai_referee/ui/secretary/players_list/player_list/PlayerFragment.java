package ru.nishty.aai_referee.ui.secretary.players_list.player_list;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        gradeNames.add("Выберите разряд игрока");
        for (int i = 1; i <= 9; i++) {
            int gradeResourceId = DataBaseContractSecretary.GradeHelper.getGrade(i);
            if (gradeResourceId != -1) {
                gradeNames.add(getContext().getString(gradeResourceId));
            }
        }

        List<Region> regions = dataBaseHelperSecretary.getRegions(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(competitionUuid));
        List<String> regionNames = new ArrayList<>();
        List<Integer> regionIds = new ArrayList<>();
        regionNames.add("Выберите регион игрока");
        for (Region region : regions) {
            regionNames.add(region.getName());
            regionIds.add(region.getId());
        }

        List<Category> categories = dataBaseHelperSecretary.getCategories(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(competitionUuid));
        List<String> categoryNames = new ArrayList<>();
        List<Integer> categoryIds = new ArrayList<>();
        categoryNames.add("Выберите категорию");

        for (Category category : categories) {
            categoryNames.add(category.getName());
            categoryIds.add(category.getId());
        }

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, gradeNames) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) { // Первый элемент списка
                    // Устанавливаем серый цвет для текста
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    // Устанавливаем обычный цвет для текста (черный, например)
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, regionNames) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) { // Первый элемент списка
                    // Устанавливаем серый цвет для текста
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    // Устанавливаем обычный цвет для текста (черный, например)
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryNames) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) { // Первый элемент списка
                    // Устанавливаем серый цвет для текста
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    // Устанавливаем обычный цвет для текста (черный, например)
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Устанавливаем адаптер для Spinner'а
        spinnerGrade.setAdapter(gradeAdapter);
        spinnerRegion.setAdapter(regionAdapter);
        spinnerCategory.setAdapter(categoryAdapter);


        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Добавить", null) // Устанавливаем обработчик в null
                .setNegativeButton("Отмена", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            addButton.setOnClickListener(view -> {
                int selectedGrade = spinnerGrade.getSelectedItemPosition();
                int selectedPositionCategory = spinnerCategory.getSelectedItemPosition() - 1;
                int selectedPositionRegion = spinnerRegion.getSelectedItemPosition() - 1;
                if (selectedGrade == 0) {
                    Toast.makeText(getActivity(), "Пожалуйста, выберите разряд", Toast.LENGTH_SHORT).show();
                } else if (selectedPositionCategory < 0) {
                    Toast.makeText(getActivity(), "Пожалуйста, выберите категорию", Toast.LENGTH_SHORT).show();
                } else if (selectedPositionRegion < 0) {
                    Toast.makeText(getActivity(), "Пожалуйста, выберите регион", Toast.LENGTH_SHORT).show();
                } else {
                    // Если все поля выбраны, выполняем действия добавления игрока
                    String name = etPlayerName.getText().toString();
                    int selectedCategory = categoryIds.get(selectedPositionCategory);
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
                        dialog.dismiss(); // Закрываем диалог только если все успешно
                    }
                }
            });
        });

        dialog.show();
    }

}
