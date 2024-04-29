package ru.nishty.aai_referee.ui.secretary.protocol;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ProtocolPagerAdapter extends FragmentStateAdapter {
    private final Protocol protocol;
    private final PerformanceSecretary performance;

    public ProtocolPagerAdapter(@NonNull Fragment fragment, Protocol protocol, PerformanceSecretary performance) {
        super(fragment);
        this.protocol = protocol;
        this.performance = performance;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Player currentPlayer = performance.getPlayers().get(position);
        return ProtocolFragment.newInstance(protocol, performance, position);
    }

    @Override
    public int getItemCount() {
        return performance.getPlayers().size();
    }
}
