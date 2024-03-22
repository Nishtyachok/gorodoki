package ru.nishty.aai_referee.ui.secretary.players_list.region_list;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.nishty.aai_referee.databinding.FragmentRegionBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// RegionFragment.java
public class RegionFragment extends Fragment {

    private FragmentRegionBinding binding;
    private RegionAdapter regionAdapter;
    private List<Region> regionList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private String competitionUuid;

    public static RegionFragment newInstance(String uuid) {
        RegionFragment fragment = new RegionFragment();
        Bundle args = new Bundle();
        args.putString("competitionUuid", uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            competitionUuid = getArguments().getString("competitionUuid");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        regionList = new ArrayList<>();
        regionAdapter = new RegionAdapter(regionList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(regionAdapter);

        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        loadRegionsFromDatabase();
    }

    private void loadRegionsFromDatabase() {
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        regionList.clear();
        regionList.addAll(dataBaseHelperSecretary.getRegions(db, UUID.fromString(competitionUuid)));
        regionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

