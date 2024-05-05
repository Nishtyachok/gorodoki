package ru.nishty.aai_referee.ui.referee.performance_list;

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

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.referee.DataBaseHelperReferee;
import ru.nishty.aai_referee.entity.referee.Protocol;
import ru.nishty.aai_referee.ui.referee.performance_list.placeholder.PerformanceContent;

/**
 * A fragment representing a list of Items.
 */
public class PerformanceFragment extends Fragment {
    private static final String ARG_ID = "i";
    private static String ID;
    private int mColumnCount = 1;
    public PerformanceFragment() {
    }

    public static PerformanceFragment newInstance(int columnCount) {
        PerformanceFragment fragment = new PerformanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, ID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            ID = getArguments().getString(ARG_ID);
        }

        View view = inflater.inflate(R.layout.fragment_performance_list, container, false);

        DataBaseHelperReferee dataBaseHelperReferee = new DataBaseHelperReferee(getContext());
        SQLiteDatabase db = dataBaseHelperReferee.getReadableDatabase();

        PerformanceContent.fill(dataBaseHelperReferee.getPerformances(db, ID));
        db.close();
        dataBaseHelperReferee.close();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(new MyPerformanceRecyclerViewAdapter
                    (
                            PerformanceContent.ITEM_MAP,
                            (performance) -> {
                                try {
                                    DataBaseHelperReferee dataBaseHelperReferee1 = new DataBaseHelperReferee(getContext());
                                    SQLiteDatabase db1 = dataBaseHelperReferee1.getReadableDatabase();
                                    Protocol protocol = dataBaseHelperReferee1.getProtocol(db1, ID, performance.getId());
                                    Bundle arg = new Bundle();

                                    arg.putSerializable("protocol", protocol);
                                    arg.putSerializable("performance", performance);

                                    NavHostFragment.findNavController(PerformanceFragment.this)
                                            .navigate(R.id.action_fragmentPerformance_to_protocolQrFragment, arg);
                                } catch (Exception e) {
                                    Protocol p = new Protocol();
                                    p.setComp_id(ID);
                                    p.setPerf_id(performance.getId());

                                    Bundle arg = new Bundle();
                                    arg.putSerializable("protocol", p);
                                    arg.putSerializable("performance", performance);


                                    NavHostFragment.findNavController(PerformanceFragment.this)
                                            .navigate(R.id.action_fragmentPerformance_to_protocolFillingFragment, arg);
                                }
                            }
                    ));
        }
        return view;
    }
}