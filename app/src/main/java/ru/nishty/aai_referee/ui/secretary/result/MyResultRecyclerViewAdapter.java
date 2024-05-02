package ru.nishty.aai_referee.ui.secretary.result;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.nishty.aai_referee.databinding.ResultItemBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class MyResultRecyclerViewAdapter extends RecyclerView.Adapter<MyResultRecyclerViewAdapter.ViewHolder> {
    private final List<ResultPlayer> mValues;
    private DataBaseHelperSecretary dataBaseHelperSecretary;

    public MyResultRecyclerViewAdapter(List<ResultPlayer> mValues) {
        this.mValues = mValues;
        // Сортировка игроков по количеству протоколов
        Collections.sort(mValues, new Comparator<ResultPlayer>() {
            @Override
            public int compare(ResultPlayer p1, ResultPlayer p2) {
                int p1ProtocolCount = p1.getProtocols() != null ? p1.getProtocols().size() : 0;
                int p2ProtocolCount = p2.getProtocols() != null ? p2.getProtocols().size() : 0;
                // Сравниваем количество протоколов
                int compareProtocolCount = Integer.compare(p2ProtocolCount, p1ProtocolCount);
                if (compareProtocolCount != 0) {
                    return compareProtocolCount;
                } else {
                    // Дополнительная сортировка для игроков с одинаковым количеством протоколов
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
        return new MyResultRecyclerViewAdapter.ViewHolder(ResultItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        dataBaseHelperSecretary = new DataBaseHelperSecretary(context);
        SQLiteDatabase dbr = dataBaseHelperSecretary.getReadableDatabase();

        // Получаем данные из базы данных для текущей позиции
        ResultPlayer player = mValues.get(position);

        holder.mNumber.setText(String.valueOf(position + 1));
        holder.mName.setText(player.getName());
        holder.mRange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(player.getRange())));
        holder.mRegion.setText(player.getRegion());



        // Обработка протоколов
        List<Protocol> protocols = player.getProtocols();
        if (protocols != null && !protocols.isEmpty()) {
            Protocol protocol1 = protocols.get(0);
            holder.mP11.setText(String.valueOf(protocol1.getGame1()));
            holder.mP12.setText(String.valueOf(protocol1.getGame2()));
            holder.mT1.setText(String.valueOf(protocol1.getGames_sum()));

            if (protocols.size() > 1) {
                Protocol protocol2 = protocols.get(1);
                holder.mP21.setText(String.valueOf(protocol2.getGame1()));
                holder.mP22.setText(String.valueOf(protocol2.getGame2()));
                holder.mT2.setText(String.valueOf(protocol2.getGames_sum()));
                holder.mT12.setText(String.valueOf(protocol2.getGames_sum() + protocol1.getGames_sum()));

                if (protocols.size() > 2) {
                    Protocol protocol3 = protocols.get(2);
                    int xx = protocol3.getGames_sum() + protocol2.getGames_sum() + protocol1.getGames_sum();
                    holder.mP31.setText(String.valueOf(protocol3.getGame1()));
                    holder.mP32.setText(String.valueOf(protocol3.getGame2()));
                    holder.mT3.setText(String.valueOf(protocol3.getGames_sum()));
                    holder.mT123.setText(String.valueOf(xx));
                    holder.mPlant.setText(String.valueOf(position + 1));
                    if(xx>=133&&xx<=140&& player.getRange()<7){
                        holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(7)));
                    }
                    if(xx>=121&&xx<=132&& player.getRange()<8){
                        holder.mNrange.setText(context.getString(DataBaseContractSecretary.GradeHelper.getGrade(8)));
                    }
                    if(xx<=120&& player.getRange()==8){
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
        public final TextView mP11;
        public final TextView mP12;
        public final TextView mP21;
        public final TextView mP22;
        public final TextView mP31;
        public final TextView mP32;
        public final TextView mT1;
        public final TextView mT2;
        public final TextView mT3;
        public final TextView mT123;
        public final TextView mT12;
        public final TextView mNumber;
        public final TextView mName;
        public final TextView mRange;
        public final TextView mRegion;
        public final TextView mPlant;
        public final TextView mNrange;

        public ViewHolder(ResultItemBinding binding) {
            super(binding.getRoot());
            mP11 = binding.p11;
            mP12 = binding.p12;
            mP21 = binding.p21;
            mP22 = binding.p22;
            mP31 = binding.p31;
            mP32 = binding.p32;
            mT1 = binding.t1;
            mT2 = binding.t2;
            mT3 = binding.t3;
            mT12 = binding.t12;
            mT123 = binding.t123;
            mNumber = binding.number;
            mName = binding.name;
            mRange = binding.range;
            mRegion = binding.region;
            mPlant = binding.plant;
            mNrange = binding.nrange;
        }
    }
}
