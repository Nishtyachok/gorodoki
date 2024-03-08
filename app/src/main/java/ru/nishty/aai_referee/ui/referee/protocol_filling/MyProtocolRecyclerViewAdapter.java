package ru.nishty.aai_referee.ui.referee.protocol_filling;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.nishty.aai_referee.databinding.FragmentProtocolFillingItemBinding;

public class MyProtocolRecyclerViewAdapter extends RecyclerView.Adapter<MyProtocolRecyclerViewAdapter.ViewHolder> {

    private final List<Integer> mValues;



    public MyProtocolRecyclerViewAdapter(List<Integer> mValues) {
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentProtocolFillingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mDisciplineView.setText(mValues.get(position));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mDisciplineView;
        public final RadioGroup mRadioGroup;


        public ViewHolder(FragmentProtocolFillingItemBinding binding) {
            super(binding.getRoot());
            mDisciplineView = binding.itemText;
            mRadioGroup = binding.radioGroup;
        }
    }
}
