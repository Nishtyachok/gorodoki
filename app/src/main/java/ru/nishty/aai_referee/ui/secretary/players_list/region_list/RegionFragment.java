package ru.nishty.aai_referee.ui.secretary.players_list.region_list;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegionFragment extends Fragment {

    private RecyclerView recyclerView;
    private RegionsAdapter regionsAdapter;
    private List<Region> regionsList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private String competitionUuid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_region, container, false);

        recyclerView = view.findViewById(R.id.rvRegions);
        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();


        // Получаем UUID соревнования из аргументов фрагмента
        Bundle arguments = getArguments();
        if (arguments != null) {
            competitionUuid = arguments.getString("competitionUuid");
        }
        regionsList = dataBaseHelperSecretary.getRegions(dbr, UUID.fromString(competitionUuid));
        if(regionsList==null)
            regionsList = new ArrayList<>();
        setupRecyclerView();
        FloatingActionButton fabAddRegion = view.findViewById(R.id.fabAddRegion);
        fabAddRegion.setOnClickListener(v -> showAddRegionDialog());

        return view;
    }

    private void setupRecyclerView() {
        regionsAdapter = new RegionsAdapter(regionsList);
        recyclerView.setAdapter(regionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showAddRegionDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_region, null);
        EditText etRegionName = dialogView.findViewById(R.id.etRegionName);



        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String name = etRegionName.getText().toString();

                    Region region = new Region();
                    region.setComp_id(UUID.fromString(competitionUuid));
                    region.setName(name);


                    SQLiteDatabase db = dataBaseHelperSecretary.getWritableDatabase();
                    long regionId = dataBaseHelperSecretary.addRegion(db, region, UUID.fromString(competitionUuid));

                    if (regionId != -1) {
                        regionsList.add(region);
                        regionsAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

}
