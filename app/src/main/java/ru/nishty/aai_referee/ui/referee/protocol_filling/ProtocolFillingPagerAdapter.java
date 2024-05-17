package ru.nishty.aai_referee.ui.referee.protocol_filling;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.nishty.aai_referee.entity.referee.Performance;
import ru.nishty.aai_referee.entity.referee.Protocol;

public class ProtocolFillingPagerAdapter extends FragmentStateAdapter {
    private final Protocol protocol;
    private final Performance performance;

    public ProtocolFillingPagerAdapter(@NonNull Fragment fragment, Protocol protocol, Performance performance) {
        super(fragment);
        this.protocol = protocol;
        this.performance = performance;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ProtocolFillingFragment.newInstance(protocol, performance, position);
    }

    @Override
    public int getItemCount() {
        return performance.getPlayers().size();
    }
}
