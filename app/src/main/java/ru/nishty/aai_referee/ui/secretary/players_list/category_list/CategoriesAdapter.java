package ru.nishty.aai_referee.ui.secretary.players_list.category_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.nishty.aai_referee.databinding.CategoryItemBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final List<Category> categoriesList;

    public CategoriesAdapter(List<Category> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoriesList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvAgeLimit;
        private final TextView tvFigures;
        private final TextView tvTours;
        private final TextView tvLimit;


        public ViewHolder(CategoryItemBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvCategoryName;
            tvAgeLimit = binding.tvAgeLimit;
            tvTours = binding.tvTours;
            tvFigures = binding.tvFigures;
            tvLimit = binding.tvLimit;


        }

        public void bind(Category category) {
            Context context = itemView.getContext();
            tvName.setText(category.getName());
            tvAgeLimit.setText(category.getAgelimit());
            int categoryConfigResourceId = DataBaseContractSecretary.CategoryHelper.getCategoryConf(category.getConfig());
            if (categoryConfigResourceId != -1) {
                tvFigures.setText(context.getString(categoryConfigResourceId));
            } else {
                tvFigures.setText("Unknown Discipline");
            }
            tvTours.setText("Туры: "+ String.valueOf(category.getTours()));
            tvLimit.setText("Лимит: "+String.valueOf(category.getLimit()));

        }
    }
}