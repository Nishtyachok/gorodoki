package ru.nishty.aai_referee.ui.secretary.competition_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.CompetitionSecretary;
import ru.nishty.aai_referee.listeners.ScanListener;
import ru.nishty.aai_referee.ui.secretary.competition_list.placeholder.CompetitionContent;


public class CompetitionFragment extends Fragment {
    FloatingActionButton scanBtn2;
    RecyclerView recyclerView;
    FloatingActionButton scanBtn;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private MyCompetitionRecyclerViewAdapter adapter;

    public CompetitionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CompetitionFragment newInstance(int columnCount) {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_competition_list_secretary, container, false);

        recyclerView = view.findViewById(R.id.competition_recycler_view);


        fill();

        adapter = new MyCompetitionRecyclerViewAdapter(CompetitionContent.ITEMS, new MyCompetitionRecyclerViewAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(CompetitionSecretary competition, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("id",competition.getUuid());
                //bundle.putInt("discipline",competition.getDiscipline());
                NavHostFragment.findNavController(CompetitionFragment.this)
                        .navigate(R.id.action_competitionFragment2_to_fragmentPerformance22,bundle);
            }
        }, new MyCompetitionRecyclerViewAdapter.OnStateClickListener(){
            @Override
            public void onStateClick(CompetitionSecretary competition, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("competition",competition);
                NavHostFragment.findNavController(CompetitionFragment.this)
                        .navigate(R.id.action_competitionFragment2_to_protocolQrFragment4,bundle);
            }
        });


        CompetitionContent.setScanListener(new ScanListener() {
            @Override
            public void onScan() {
                update();
            }
        });

        // Set the adapter

        Context context = view.getContext();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(adapter);


        scanBtn = view.findViewById(R.id.add_competition_button);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(CompetitionFragment.this).navigate(R.id.action_competitionFragment2_to_competitionAddFragment);

            }
        });
        scanBtn2 = view.findViewById(R.id.scan_competition_button);

        scanBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ru.nishty.aai_referee.ui.referee.competition_list.placeholder.CompetitionContent.onClick();
            }
        });


        return view;
    }

    public void update(){
        fill();
        adapter.update();
    }

    public void fill(){
        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        CompetitionContent.fill(dataBaseHelperSecretary.getCompetitions(db));
    }

}