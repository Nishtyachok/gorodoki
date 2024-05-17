package ru.nishty.aai_referee.ui.secretary.result;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import ru.nishty.aai_referee.entity.secretary.Category;

public class ResultPagerAdapter extends FragmentStateAdapter {
    private final String id;
    private final List<Category> categories;

    public ResultPagerAdapter(@NonNull Fragment fragment, String id, List<Category> categories) {
        super(fragment);
        this.id = id;
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Category category = categories.get(position);
        return ResultFragment.newInstance(id, category.getId(), position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
