package ru.nishty.aai_referee.ui.secretary.judge_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.databinding.JudgeItemBinding;
import ru.nishty.aai_referee.entity.secretary.Judge;

import java.util.List;

public class JudgesAdapter extends RecyclerView.Adapter<JudgesAdapter.ViewHolder> {

    private final List<Judge> judgesList;

    public JudgesAdapter(List<Judge> judgesList) {
        this.judgesList = judgesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(JudgeItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Judge judge = judgesList.get(position);
        holder.bind(judge);
    }

    @Override
    public int getItemCount() {
        return judgesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvRegion;
        private final TextView tvCategory;

        public ViewHolder(JudgeItemBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvJudgeName;
            tvRegion = binding.tvJudgeRegion;
            tvCategory = binding.tvJudgeCategory;

        }

        public void bind(Judge judge) {
            tvName.setText(judge.getName());
            tvRegion.setText(judge.getRegion());
            tvCategory.setText(judge.getCategory());
        }
    }
}