package ru.nishty.aai_referee.ui.secretary.result;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ResultPagerFragment extends Fragment {
    private static final String ARG_ID = "id";
    private static String ID;

    private Player player;
    private View view;

    private ViewPager2 view_Pager_2;
    private TabLayout tab_Layout;
    private ResultPagerAdapter pagerAdapter;
    private int currentPage = 0;
    private static Protocol protocol;
    private static PerformanceSecretary performance;


    public static ResultPagerFragment newInstance(String id) {
        ResultPagerFragment fragment = new ResultPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ID = getArguments().getString(ARG_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            ID = getArguments().getString(ARG_ID);
        }
        view = inflater.inflate(R.layout.fragment_resilt_pager, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();

        toolbar.setTitle(ID);

        view_Pager_2 = view.findViewById(R.id.view_Pager_2);
        tab_Layout = view.findViewById(R.id.tab_Layout_result);
        List<Category> categories = dataBaseHelperSecretary.getCategories(db,ID);

        pagerAdapter = new ResultPagerAdapter(this, ID, categories);
        view_Pager_2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tab_Layout, view_Pager_2, (tab, position) -> {
            String catName = categories.get(position).getName();
            tab.setText(catName);
        }).attach();
        view_Pager_2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                toolbar.setTitle(categories.get(currentPage).getName());

            }
        });


        return view;
    }


    private String formatPlayerName(String name) {
        String[] parts = name.split(" ");
        if (parts.length == 1) {
            return parts[0];
        } else {
            String firstName = parts[0];
            String lastNameInitial = parts[parts.length - 1].charAt(0) + ".";
            return firstName + " " + lastNameInitial;
        }
    }
}