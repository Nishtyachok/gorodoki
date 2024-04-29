package ru.nishty.aai_referee.ui.secretary.protocol;

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

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ProtocolPlayerFragment extends Fragment {
    private static final String ARG_PLAYER = "player";
    private Player player;
    private View view;
    private static final String ARG_PROTOCOL = "protocol";
    private static final String ARG_PERFORMANCE = "performance";


    private ViewPager2 view_Pager_2;
    private TabLayout tab_Layout;
    private ProtocolPagerAdapter pagerAdapter;
    private int currentPage = 0;
    private static Protocol protocol;
    private static PerformanceSecretary performance;


    public static ProtocolPlayerFragment newInstance(Player player, Protocol protocol, PerformanceSecretary performance) {
        ProtocolPlayerFragment fragment = new ProtocolPlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLAYER, player);
        args.putSerializable(ARG_PROTOCOL, protocol);
        args.putSerializable(ARG_PERFORMANCE, performance);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            player = (Player) getArguments().getSerializable("player");
            performance = (PerformanceSecretary) getArguments().getSerializable("performance");
            protocol = (Protocol) getArguments().getSerializable("protocol");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            protocol = (Protocol) getArguments().getSerializable("protocol");
            performance = (PerformanceSecretary) getArguments().getSerializable(ARG_PERFORMANCE);
        }
        view = inflater.inflate(R.layout.fragment_protocol_pager, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(performance.getPlayers().get(currentPage).getName());
        view_Pager_2 = view.findViewById(R.id.view_Pager_2);
        tab_Layout = view.findViewById(R.id.tab_Layout);
        pagerAdapter = new ProtocolPagerAdapter(this, protocol, performance);
        view_Pager_2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tab_Layout, view_Pager_2, (tab, position) -> {
            String playerName = formatPlayerName(performance.getPlayers().get(position).getName());
            tab.setText(playerName);
        }).attach();
        view_Pager_2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                toolbar.setTitle(performance.getPlayers().get(currentPage).getName());

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
