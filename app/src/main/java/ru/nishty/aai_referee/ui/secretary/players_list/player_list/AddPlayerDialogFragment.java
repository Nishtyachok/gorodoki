package ru.nishty.aai_referee.ui.secretary.players_list.player_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.DialogFragment;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.secretary.Category;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Region;
import ru.nishty.aai_referee.ui.secretary.players_list.category_list.CategoriesAdapter;
import ru.nishty.aai_referee.ui.secretary.players_list.region_list.RegionsAdapter;

import java.util.List;

public class AddPlayerDialogFragment extends DialogFragment {

    private EditText etPlayerName;
    private Spinner spinnerCategory, spinnerRegion;
    private Button btnAddPlayer;

    private List<Category> categories;
    private List<Region> regions;

    private OnPlayerAddedListener listener;

    public AddPlayerDialogFragment(List<Category> categories, List<Region> regions, OnPlayerAddedListener listener) {
        this.categories = categories;
        this.regions = regions;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_player, container, false);

        etPlayerName = view.findViewById(R.id.etPlayerName);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerRegion = view.findViewById(R.id.spinnerRegion);
        btnAddPlayer = view.findViewById(R.id.btnAddPlayer);

        // Устанавливаем адаптеры для спиннеров с категориями и регионами
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categories);
        spinnerCategory.setAdapter((SpinnerAdapter) categoriesAdapter);

        RegionsAdapter regionsAdapter = new RegionsAdapter( regions);
        spinnerRegion.setAdapter((SpinnerAdapter) regionsAdapter);

        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer();
            }
        });

        return view;
    }

    private void addPlayer() {
        String playerName = etPlayerName.getText().toString().trim();
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        Region selectedRegion = (Region) spinnerRegion.getSelectedItem();

        if (!playerName.isEmpty()) {
            Player player = new Player();
            player.setName(playerName);
            player.setCategoryId(selectedCategory.getId());
            player.setRegionId(selectedRegion.getId());

            // Уведомляем слушателя о добавлении нового игрока
            if (listener != null) {
                listener.onPlayerAdded(player);
            }

            dismiss();
        } else {
            Toast.makeText(getContext(), "Введите имя игрока", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPlayerAddedListener {
        void onPlayerAdded(Player player);
    }
}

