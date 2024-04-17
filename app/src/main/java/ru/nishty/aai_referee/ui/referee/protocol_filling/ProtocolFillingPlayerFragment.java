package ru.nishty.aai_referee.ui.referee.protocol_filling;

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
import ru.nishty.aai_referee.entity.referee.Performance;
import ru.nishty.aai_referee.entity.referee.PlayerRef;
import ru.nishty.aai_referee.entity.referee.Protocol;

public class ProtocolFillingPlayerFragment extends Fragment {
    private static final String ARG_PLAYER = "player";
    private PlayerRef player;
    private View view;
    private static final String ARG_PROTOCOL = "protocol";
    private static final String ARG_PERFORMANCE = "performance";

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ProtocolFillingPagerAdapter pagerAdapter;
    private int currentPage = 0;
    private static Protocol protocol;
    private static Performance performance;


    public static ProtocolFillingPlayerFragment newInstance(PlayerRef player, Protocol protocol, Performance performance) {
        ProtocolFillingPlayerFragment fragment = new ProtocolFillingPlayerFragment();
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
            player = (PlayerRef) getArguments().getSerializable("player");
            protocol = (Protocol) getArguments().getSerializable("protocol");
            performance = (Performance) getArguments().getSerializable("performance");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            protocol = (Protocol) getArguments().getSerializable(ARG_PROTOCOL);
            performance = (Performance) getArguments().getSerializable(ARG_PERFORMANCE);
        }
        view = inflater.inflate(R.layout.fragment_protocol_filling_pager, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(performance.getPlayers().get(currentPage).getName());
        viewPager2 = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tabLayout);

        pagerAdapter = new ProtocolFillingPagerAdapter(this, protocol, performance);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            String playerName = formatPlayerName(performance.getPlayers().get(position).getName());
            tab.setText(playerName);
        }).attach();
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
