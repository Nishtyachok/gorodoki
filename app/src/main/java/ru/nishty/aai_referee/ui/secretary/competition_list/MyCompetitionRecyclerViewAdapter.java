package ru.nishty.aai_referee.ui.secretary.competition_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.databinding.CompetitionItemBinding;
import ru.nishty.aai_referee.entity.referee.Competition;

import java.util.List;


public class MyCompetitionRecyclerViewAdapter extends RecyclerView.Adapter<MyCompetitionRecyclerViewAdapter.ViewHolder> {

    private final List<Competition> mValues;

    private final OnStateClickListener onStateClickListener;

    public interface OnStateClickListener{
        void onStateClick(Competition competition, int position);

    }

    public MyCompetitionRecyclerViewAdapter(List<Competition> items, OnStateClickListener onStateClickListener) {
        mValues = items;
        this.onStateClickListener = onStateClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(CompetitionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int p = position;
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(String.valueOf(mValues.get(position).getYear()));
        holder.mItem3.setText(String.valueOf(mValues.get(position).getPlace()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStateClickListener.onStateClick(mValues.get(p),p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public void update(){

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;

        public final TextView mItem3;
        public final TextView mContentView;
        public Competition mItem;

        public ViewHolder(CompetitionItemBinding binding) {
            super(binding.getRoot());
            mItem3 = binding.content3;
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }



        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}