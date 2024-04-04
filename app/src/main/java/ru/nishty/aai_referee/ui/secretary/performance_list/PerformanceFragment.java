package ru.nishty.aai_referee.ui.secretary.performance_list;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Protocol;

/**
 * A fragment representing a list of Items.
 */
public class PerformanceFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static UUID ID;
    private int mColumnCount = 1;

    public PerformanceFragment() {
    }

    public static PerformanceFragment newInstance(UUID id) {
        PerformanceFragment fragment = new PerformanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            ID = UUID.fromString(getArguments().getString(ARG_ID));
        }
        View view = inflater.inflate(R.layout.fragment_performance_list_secretary, container, false);
        FloatingActionButton addButton = view.findViewById(R.id.add_performance_button);
        addButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(ARG_ID, ID.toString());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_fragmentPerformance2_to_performanceAddFragment, args);
        });

        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        List<PerformanceSecretary> performances = dataBaseHelperSecretary.getPerformances(db, ID);
        db.close();
        dataBaseHelperSecretary.close();

        RecyclerView recyclerView = view.findViewById(R.id.performance_recycler_view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyPerformanceRecyclerViewAdapter(performances, performance -> {
            DataBaseHelperSecretary dataBaseHelperSecretary1 = new DataBaseHelperSecretary(getContext());
            SQLiteDatabase db1 = dataBaseHelperSecretary1.getReadableDatabase();
            Protocol protocol = dataBaseHelperSecretary1.getProtocol(db1, ID, performance.getId());
            db1.close();
            dataBaseHelperSecretary1.close();
            if (protocol == null) {
                Bundle args = new Bundle();
                args.putSerializable("performance", performance);
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_fragmentPerformance2_to_protocolQrFragment2, args);
            } else {

            }
        }));

        return view;
    }
}
