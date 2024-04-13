package ru.nishty.aai_referee.ui.referee.performance_list;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.nishty.aai_referee.databinding.PerformanceItemBinding;
import ru.nishty.aai_referee.entity.referee.Performance;
import ru.nishty.aai_referee.entity.referee.PlayerRef;


public class MyPerformanceRecyclerViewAdapter extends RecyclerView.Adapter<MyPerformanceRecyclerViewAdapter.ViewHolder> {

    private final List<Performance> mValues;

    private final OnStateClickListener onStateClickListener;

    public interface OnStateClickListener {
        void onStateClick(Performance performance);

    }

    public MyPerformanceRecyclerViewAdapter(List<Performance> items, OnStateClickListener onStateClickListener) {
        mValues = items;
        this.onStateClickListener = onStateClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(PerformanceItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int p = position;
        holder.mIdView.setText(String.valueOf(position));
        holder.mPlaceView.setText(mValues.get(position).getPlace());
        holder.mDateView.setText(mValues.get(position).getDate());
        holder.mPlaygroundView.setText(mValues.get(position).getPlayground() + " площадка");
        holder.mTimeView.setText(mValues.get(position).getTime());

        List<PlayerRef> players = mValues.get(position).getPlayers();
        if (players != null && !players.isEmpty()) {
            StringBuilder namesBuilder = new StringBuilder();
            Set<String> categories = new HashSet<>();

            for (PlayerRef player : players) {
                String name = player.getName();
                int spaceIndex = name.lastIndexOf(' ');
                if (spaceIndex != -1) {
                    if (namesBuilder.length() > 0) {
                        namesBuilder.append(" - ");
                    }
                    String lastName = name.substring(0, spaceIndex);
                    String initials = name.substring(spaceIndex + 1, spaceIndex + 2) + ".";
                    namesBuilder.append(lastName).append(" ").append(initials);
                    categories.add(player.getCategory());
                } else {
                    if (namesBuilder.length() > 0) {
                        namesBuilder.append(" - ");
                    }
                    namesBuilder.append(name);
                    categories.add(player.getCategory());
                }
            }

            holder.mContentView.setText(namesBuilder.toString());

            if (categories.size() == 1) {
                holder.mCategoryView.setText(categories.iterator().next());
            } else {
                holder.mCategoryView.setText(TextUtils.join(", ", categories));
            }
        } else {
            holder.mContentView.setText("Нет игроков");
            holder.mCategoryView.setText("Нет категорий");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStateClickListener.onStateClick(mValues.get(p));
            }
        });
    }

    public void update(List<Performance> performances) {
        mValues.clear();
        mValues.addAll(performances);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mPlaygroundView;
        public final TextView mDateView;
        public final TextView mTimeView;
        public final TextView mPlaceView;
        public final TextView mCategoryView;

        public ViewHolder(PerformanceItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mPlaygroundView = binding.playground;
            mDateView = binding.date;
            mTimeView = binding.time;
            mPlaceView = binding.place;
            mCategoryView = binding.category;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}