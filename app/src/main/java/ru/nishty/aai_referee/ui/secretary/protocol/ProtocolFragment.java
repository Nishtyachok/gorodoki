package ru.nishty.aai_referee.ui.secretary.protocol;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ProtocolFragment extends Fragment {
    private View view;
    private static final String ARG_PROTOCOL = "protocol";
    private static final String ARG_PERFORMANCE = "performance";

    private static Protocol protocol;
    private static PerformanceSecretary performance;
    private ProtocolPagerAdapter pagerAdapter;

    private int currentPage = 0;
    private int position;

    public ProtocolFragment() {
    }

    public static ProtocolFragment newInstance(Protocol protocol, PerformanceSecretary performance, int position) {
        ProtocolFragment fragment = new ProtocolFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("protocol", protocol);
        args.putSerializable("performance", performance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
            SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
            performance = (PerformanceSecretary) getArguments().getSerializable("performance");
            currentPage = getArguments().getInt("position");
            protocol = dataBaseHelperSecretary.getProtocolPlayer(db, performance.getComp_id(), performance.getId(),currentPage);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
            SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
            position = getArguments().getInt("position");
            performance = (PerformanceSecretary) getArguments().getSerializable(ARG_PERFORMANCE);
            protocol = dataBaseHelperSecretary.getProtocolPlayer(db, performance.getComp_id(), performance.getId(),currentPage);
            List<Integer> protocols = dataBaseHelperSecretary.getProtocolsByPlayer(db,performance.getComp_id(),performance.getPlayers().get(currentPage).getId());
            List<Integer> imp = protocols;
        }

        view = inflater.inflate(R.layout.fragment_protocol, container, false);
        TableLayout tableLayout = view.findViewById(R.id.tableLayout);
        List<TableRow> tableRows = new ArrayList<>();
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                tableRows.add((TableRow) child);
            }
        }

        List<List<String>> firstGameShots = parseShots(protocol.getShots1());
        List<List<String>> secondGameShots = parseShots(protocol.getShots2());
        for (int rowIndex = 1; rowIndex < 18; rowIndex++) {
            TableRow tableRow = tableRows.get(rowIndex);

            List<TextView> textViews = new ArrayList<>();
            for (int i = 0; i < tableRow.getChildCount(); i++) {
                View child = tableRow.getChildAt(i);
                if (child instanceof TextView) {
                    textViews.add((TextView) child);
                }
            }
            switch (rowIndex) {
                case 16:
                    int g1 = protocol.getGame1();
                    int g2 = protocol.getGame2();
                    textViews.get(1).setText(String.valueOf(g1));
                    textViews.get(2).setText(String.valueOf(g2));
                    continue;
                case 17:
                    int gs = protocol.getGames_sum();
                    textViews.get(1).setText(String.valueOf(gs));
                    continue;
            }
            List<String> currentFirstGameShots = firstGameShots.get(rowIndex - 1);
            List<String> currentSecondGameShots = secondGameShots.get(rowIndex - 1);

            textViews.get(1).setText(formatShots(currentFirstGameShots));
            textViews.get(2).setText(formatShots(currentSecondGameShots));
        }

        return view;
    }
    private String formatShots(List<String> shots) {
        StringBuilder sb = new StringBuilder();
        for (String shot : shots) {
            sb.append(shot).append(" ");
        }
        return sb.toString().trim();
    }
    private List<List<String>> parseShots(String shotsString) {
        List<List<String>> shotsList = new ArrayList<>();
        if (shotsString != null && !shotsString.isEmpty()) {
            shotsString = shotsString.trim().replaceAll("^\\[|\\]$", "");
            String[] rowTokens = shotsString.split("\\], \\[");

            for (String row : rowTokens) {
                List<String> rowList = new ArrayList<>();
                String[] items = row.replaceAll("^\\[|\\]$", "").split(", ");
                for (String item : items) {
                    rowList.add(item);
                }
                shotsList.add(rowList);
            }
        }
        return shotsList;
    }


}