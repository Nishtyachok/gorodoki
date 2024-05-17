package ru.nishty.aai_referee.ui.secretary.result;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.nishty.aai_referee.databinding.ResultItem1Binding;
import ru.nishty.aai_referee.databinding.ResultItem2Binding;
import ru.nishty.aai_referee.databinding.ResultItemBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class MyResultRecyclerViewAdapter extends RecyclerView.Adapter<MyResultRecyclerViewAdapter.ViewHolder> {
    private final List<ResultPlayer> mValues;
    private int tour;
    private DataBaseHelperSecretary dataBaseHelperSecretary;

    public MyResultRecyclerViewAdapter(List<ResultPlayer> mValues, int tours) {

        tour = tours;
        this.mValues = mValues;
        Collections.sort(mValues, new Comparator<ResultPlayer>() {
            @Override
            public int compare(ResultPlayer p1, ResultPlayer p2) {
                int p1ProtocolCount = p1.getProtocols() != null ? p1.getProtocols().size() : 0;
                int p2ProtocolCount = p2.getProtocols() != null ? p2.getProtocols().size() : 0;
                int compareProtocolCount = Integer.compare(p2ProtocolCount, p1ProtocolCount);
                if (compareProtocolCount != 0) {
                    return compareProtocolCount;
                } else {
                    if (p1ProtocolCount == 3) {
                        return Integer.compare(getT123Sum(p1), getT123Sum(p2));
                    } else if (p1ProtocolCount == 2) {
                        return Integer.compare(getT12Sum(p1), getT12Sum(p2));
                    } else {
                        return Integer.compare(getT1Sum(p1), getT1Sum(p2));
                    }
                }
            }
        });
    }

    private int getT123Sum(ResultPlayer player) {
        List<Protocol> protocols = player.getProtocols();
        int sum = 0;
        for (Protocol protocol : protocols) {
            sum += protocol.getGames_sum();
        }
        return sum;
    }

    private int getT12Sum(ResultPlayer player) {
        List<Protocol> protocols = player.getProtocols();
        if (protocols.size() >= 2) {
            return protocols.get(0).getGames_sum() + protocols.get(1).getGames_sum();
        }
        return 0;
    }

    private int getT1Sum(ResultPlayer player) {
        List<Protocol> protocols = player.getProtocols();
        if (!protocols.isEmpty()) {
            return protocols.get(0).getGames_sum();
        }
        return 0;
    }

    @Override
    public MyResultRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewType = tour;
        switch (viewType) {
            case 1:
                return new ViewHolder(ResultItem1Binding.inflate(inflater, parent, false).getRoot());
            case 2:
                return new ViewHolder(ResultItem2Binding.inflate(inflater, parent, false).getRoot());
            case 3:
                return new ViewHolder(ResultItemBinding.inflate(inflater, parent, false).getRoot());
            default:
                throw new IllegalArgumentException("Invalid viewType: " + viewType);
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        dataBaseHelperSecretary = new DataBaseHelperSecretary(context);
        SQLiteDatabase dbr = dataBaseHelperSecretary.getReadableDatabase();
        ResultPlayer player = mValues.get(position);
        holder.mNumber.setText(String.valueOf(position + 1));
        holder.mName.setText(player.getName());
        holder.mRange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(player.getRange())));
        holder.mRegion.setText(player.getRegion());

        List<Protocol> protocols = player.getProtocols();
        if (protocols != null && !protocols.isEmpty()) {
            Protocol protocol1 = protocols.get(0);
            holder.mP11.setText(String.valueOf(protocol1.getGame1()));
            holder.mP12.setText(String.valueOf(protocol1.getGame2()));
            holder.mT1.setText(String.valueOf(protocol1.getGames_sum()));
            if (tour == 1)
                holder.mPlant.setText(String.valueOf(position + 1));

            if (protocols.size() > 1 && tour > 1) {
                Protocol protocol2 = protocols.get(1);
                int x2 = protocol2.getGames_sum() + protocol1.getGames_sum();
                holder.mP21.setText(String.valueOf(protocol2.getGame1()));
                holder.mP22.setText(String.valueOf(protocol2.getGame2()));
                holder.mT2.setText(String.valueOf(protocol2.getGames_sum()));
                if (tour == 2)
                    holder.mPlant.setText(String.valueOf(position + 1));
                holder.mT12.setText(String.valueOf(protocol2.getGames_sum() + protocol1.getGames_sum()));
                if (x2 >= 125 && x2 <= 140 && player.getRange() < 4) {
                    holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(4)));
                }
                if (x2 >= 109 && x2 <= 124 && player.getRange() < 5) {
                    holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(5)));
                }
                if (x2 <= 108 && player.getRange() < 6) {
                    holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(6)));
                }
                if (protocols.size() > 2 && tour > 2) {
                    Protocol protocol3 = protocols.get(2);
                    int x3 = protocol3.getGames_sum() + protocol2.getGames_sum() + protocol1.getGames_sum();
                    holder.mP31.setText(String.valueOf(protocol3.getGame1()));
                    holder.mP32.setText(String.valueOf(protocol3.getGame2()));
                    holder.mT3.setText(String.valueOf(protocol3.getGames_sum()));
                    holder.mT123.setText(String.valueOf(x3));
                    if (tour == 3)
                        holder.mPlant.setText(String.valueOf(position + 1));
                    holder.mPlant.setText(String.valueOf(position + 1));
                    if (x3 >= 133 && x3 <= 140 && player.getRange() < 7) {
                        holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(7)));
                    }
                    if (x3 <= 132 && player.getRange() < 8) {
                        holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(8)));
                    }
                    if (x3 <= 120 && player.getRange() == 8) {
                        holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(9)));
                    }

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mP11;
        public TextView mP12;
        public TextView mP21;
        public TextView mP22;
        public TextView mP31;
        public TextView mP32;
        public TextView mT1;
        public TextView mT2;
        public TextView mT3;
        public TextView mT123;
        public TextView mT12;
        public TextView mNumber;
        public TextView mName;
        public TextView mRange;
        public TextView mRegion;
        public TextView mPlant;
        public TextView mNrange;

        public ViewHolder(View view) {
            super(view);
            // Привязка происходит на основе viewType, который уже заложен в 'view'
            bindViews(view, tour);
        }

        private void bindViews(View view, int viewType) {
            switch (viewType) {
                case 1:
                    ResultItem1Binding binding1 = ResultItem1Binding.bind(view);
                    mP11 = binding1.p11;
                    mP12 = binding1.p12;
                    mT1 = binding1.t1;
                    mNumber = binding1.number;
                    mName = binding1.name;
                    mRange = binding1.range;
                    mRegion = binding1.region;
                    mPlant = binding1.plant;
                    mNrange = binding1.nrange;
                    break;
                case 2:
                    ResultItem2Binding binding2 = ResultItem2Binding.bind(view);
                    mP11 = binding2.p11;
                    mP12 = binding2.p12;
                    mT1 = binding2.t1;
                    mNumber = binding2.number;
                    mName = binding2.name;
                    mRange = binding2.range;
                    mRegion = binding2.region;
                    mPlant = binding2.plant;
                    mT2 = binding2.t2;
                    mT12 = binding2.t12;
                    mNrange = binding2.nrange;
                    mP21 = binding2.p21;
                    mP22 = binding2.p22;
                    break;
                case 3:
                    ResultItemBinding binding3 = ResultItemBinding.bind(view);
                    mP11 = binding3.p11;
                    mP12 = binding3.p12;
                    mT1 = binding3.t1;
                    mNumber = binding3.number;
                    mName = binding3.name;
                    mRange = binding3.range;
                    mRegion = binding3.region;
                    mPlant = binding3.plant;
                    mNrange = binding3.nrange;
                    mP21 = binding3.p21;
                    mP22 = binding3.p22;
                    mP31 = binding3.p31;
                    mP32 = binding3.p32;
                    mT2 = binding3.t2;
                    mT3 = binding3.t3;
                    mT12 = binding3.t12;
                    mT123 = binding3.t123;
                    break;
            }
        }
    }
}
