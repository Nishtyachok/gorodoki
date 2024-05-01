package ru.nishty.aai_referee.ui.secretary.competition_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.nishty.aai_referee.databinding.CompetitionItemSecretaryBinding;
import ru.nishty.aai_referee.entity.secretary.CompetitionSecretary;


public class MyCompetitionRecyclerViewAdapter extends RecyclerView.Adapter<MyCompetitionRecyclerViewAdapter.ViewHolder> {

    private final List<CompetitionSecretary> mValues;

    private final OnStateClickListener onStateClickListener;
    private final OnStateClickListener onStateClickListener2;
    private final OnStateClickListener onStateClickListener3;


    public interface OnStateClickListener{
        void onStateClick(CompetitionSecretary competition, int position);

    }


    public MyCompetitionRecyclerViewAdapter(List<CompetitionSecretary> items, OnStateClickListener onStateClickListener, OnStateClickListener onStateClickListener2,OnStateClickListener onStateClickListener3) {
        mValues = items;
        this.onStateClickListener = onStateClickListener;
        this.onStateClickListener2 = onStateClickListener2;
        this.onStateClickListener3 = onStateClickListener3;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(CompetitionItemSecretaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));


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

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStateClickListener2.onStateClick(mValues.get(p),p);
            }
        });
        holder.mResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStateClickListener3.onStateClick(mValues.get(p),p);
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
        public final Button mButton;
        public final Button mResult;
        public final TextView mContentView;
        public CompetitionSecretary mItem;

        public ViewHolder(CompetitionItemSecretaryBinding binding) {
            super(binding.getRoot());
            mItem3 = binding.content3;
            mIdView = binding.itemNumber;
            mButton = binding.button;
            mResult = binding.result;
            mContentView = binding.content;
        }



        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}