package ru.nishty.aai_referee.ui.secretary.performance_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.nishty.aai_referee.databinding.PerformanceItemSecretaryBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;


public class MyPerformanceRecyclerViewAdapter extends RecyclerView.Adapter<MyPerformanceRecyclerViewAdapter.ViewHolder> {

    private final List<PerformanceSecretary>  mValues;

    private final OnStateClickListener onStateClickListener;
    private DataBaseHelperSecretary dataBaseHelperSecretary;



    public interface OnStateClickListener{

        void onStateClick(PerformanceSecretary performance);

    }
    public String getPlayersName(List<Player> players) {
        StringBuilder playersNames = new StringBuilder();
        for (Player player : players) {
            playersNames.append(player.getName());
            playersNames.append("   ");
        }
        return playersNames.toString();
    }

    public MyPerformanceRecyclerViewAdapter(List<PerformanceSecretary> items, OnStateClickListener onStateClickListener) {
        mValues = items;
        this.onStateClickListener = onStateClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(PerformanceItemSecretaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int p = position;
        Context context = holder.itemView.getContext();
        dataBaseHelperSecretary = new DataBaseHelperSecretary(context);
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();
        holder.mIdView.setText(String.valueOf(position));
        holder.mPlayersView.setText(String.valueOf(getPlayersName( mValues.get(position).getPlayers())));
        holder.mJudgeView.setText( dataBaseHelperSecretary.getJudgeNames(dbr,mValues.get(position).getJudgeId()));
        holder.mPlaceView.setText( mValues.get(position).getPlace());
        holder.mDateView.setText(mValues.get(position).getDate());
        holder.mPlaygroundView.setText( mValues.get(position).getPlayground() + " площадка");
        holder.mCategoryView.setText(String.valueOf(dataBaseHelperSecretary.getCategoryNames(dbr,mValues.get(position).getPlayers())));
        holder.mTimeView.setText( mValues.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStateClickListener.onStateClick(mValues.get(p));
            }
        });
    }

    public void update(List<PerformanceSecretary> performances){
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

        public final TextView mPlayersView;
        public final TextView mJudgeView;
        public final TextView mPlaygroundView;
        public final TextView mDateView;
        public final TextView mTimeView;
        public final TextView mPlaceView;
        public final TextView mCategoryView;


        public ViewHolder(PerformanceItemSecretaryBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mJudgeView = binding.judge;
            mPlayersView=binding.players;
            mPlaygroundView = binding.playground;
            mDateView = binding.date;
            mTimeView = binding.addtime;
            mPlaceView = binding.place;
            mCategoryView = binding.performanceCategory;
        }



        @Override
        public String toString() {
            return super.toString() + " '" + mJudgeView.getText() + "'";
        }
    }

}