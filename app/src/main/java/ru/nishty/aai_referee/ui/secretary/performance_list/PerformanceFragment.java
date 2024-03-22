package ru.nishty.aai_referee.ui.secretary.performance_list;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import java.util.UUID;

/**
 * A fragment representing a list of Items.
 */
public class PerformanceFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ID = "id";
    private static final String ARG_DISCIPLINE = "discipline";
    private static UUID ID;
    private static int discipline;
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PerformanceFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PerformanceFragment newInstance(int columnCount) {
        PerformanceFragment fragment = new PerformanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, ID.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            ID = UUID.fromString(getArguments().getString(ARG_ID));
            discipline = getArguments().getInt(ARG_DISCIPLINE);
        }

        View view = inflater.inflate(R.layout.fragment_performance_list, container, false);

        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();

       // PerformanceContent.fill(dataBaseHelperSecretary.getPerformances(db, CompetitionSecretary.getUuid()));
        db.close();
        dataBaseHelperSecretary.close();


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ;

            recyclerView.setAdapter(new MyPerformanceRecyclerViewAdapter
                    (
                            PerformanceContent.ITEM_MAP,
                            (performance) -> {

                                try {
                                    DataBaseHelperSecretary dataBaseHelperSecretary1 = new DataBaseHelperSecretary(getContext());
                                    SQLiteDatabase db1 = dataBaseHelperSecretary1.getReadableDatabase();
                                    Protocol protocol = dataBaseHelperSecretary1.getProtocol(db1,ID,performance.getId());
                                    //TODO: set proper name
                                    protocol.set("test");

                                    Bundle arg = new Bundle();
                                    arg.putSerializable("protocol",protocol);
                                    arg.putInt(ARG_DISCIPLINE,discipline);
                                    arg.putString("name",performance.getName());
                                    arg.putString("date",performance.getDate());
                                    arg.putString("region",performance.getRegion());
                                    arg.putString("time",performance.getTime());
                                    arg.putString("category",performance.getCategory());
                                    arg.putString("grade",performance.getGrade());
                                    arg.putString("playground",performance.getPlayground());
                                    NavHostFragment.findNavController(PerformanceFragment.this)
                                            .navigate(R.id.action_fragmentPerformance_to_protocolQrFragment,
                                                    arg);
                                }
                                catch (Exception e){
                                    Protocol p = new Protocol();
                                    p.setComp_id(ID);
                                    p.setPerf_id(performance.getId());

                                    Bundle arg = new Bundle();
                                    arg.putSerializable("protocol",p);
                                    arg.putInt(ARG_DISCIPLINE,discipline);
                                    arg.putString("name",performance.getName());
                                    arg.putString("date",performance.getDate());
                                    arg.putString("region",performance.getRegion());
                                    arg.putString("time",performance.getTime());
                                    arg.putString("category",performance.getCategory());
                                    arg.putString("grade",performance.getGrade());
                                    arg.putString("playground",performance.getPlayground());


                                    NavHostFragment.findNavController(
                                        PerformanceFragment.this)
                                        .navigate(R.id.action_fragmentPerformance_to_protocolFillingFragment
                                                ,arg);
                                }
                            }
                            ));
        }
        return view;
    }

     */
}