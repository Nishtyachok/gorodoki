package ru.nishty.aai_referee.ui.secretary.players_list.category_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.secretary.Category;

import java.util.List;

// CategoryAdapter.java
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewDetails;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDetails = itemView.findViewById(R.id.textViewDetails);
        }

        public void bind(Category category) {
            textViewName.setText(category.getName());
            textViewDetails.setText("Tours: " + category.getTours() + ", Figures: " + category.getFigures());
        }
    }
}


