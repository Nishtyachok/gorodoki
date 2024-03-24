package ru.nishty.aai_referee.ui.secretary.players_list.player_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.databinding.PlayerItemBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Region;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private final List<Player> playersList;


    public PlayerAdapter(List<Player> playersList) {
        this.playersList = playersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(PlayerItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = playersList.get(position);
        holder.bind(player);
    }

    @Override
    public int getItemCount() {
        return playersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvGrade;
        private final TextView tvPlayerCategory;
        private final TextView tvPlayerRegion;
        private DataBaseHelperSecretary dataBaseHelperSecretary;
        private String competitionUuid;





        public ViewHolder(PlayerItemBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvPlayerName;
            tvGrade = binding.tvGrade;
            tvPlayerCategory = binding.tvPlayerCategory;
            tvPlayerRegion = binding.tvPlayerRegion;


        }

        public void bind(Player player) {
            Context context = itemView.getContext(); // Получаем контекст из itemView
            dataBaseHelperSecretary = new DataBaseHelperSecretary(context);
            SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();
            tvName.setText(player.getName());

            int gradeResourceId = DataBaseContractSecretary.GradeHelper.getGrade(player.getGrade());
            if (gradeResourceId != -1) {
                tvGrade.setText(context.getString(gradeResourceId));
            } else {
                tvGrade.setText("Unknown Grade");
            }

            List<Region> regions = dataBaseHelperSecretary.getRegions(dbr, player.getComp_id());
            String regionName = getRegionNameById(player.getRegionId(), regions);
            if (regionName != null) {
                tvPlayerRegion.setText(regionName);
            } else {
                tvPlayerRegion.setText("Unknown Grade");
            }

            List<Category> categories = dataBaseHelperSecretary.getCategories(dbr, player.getComp_id());
            String categoryName = getCategoryNameById(player.getCategoryId(), categories);
            if (categoryName != null) {
                tvPlayerCategory.setText(categoryName);
            } else {
                tvPlayerCategory.setText("Unknown Grade");
            }

        }
        private String getCategoryNameById(int categoryId, List<Category> categories) {
            for (Category category : categories) {
                if (category.getId() == categoryId) {
                    return category.getName();
                }
            }
            return "Unknown Region";
        }

        private String getRegionNameById(int regionId, List<Region> regions) {
            for (Region region : regions) {
                if (region.getId() == regionId) {
                    return region.getName();
                }
            }
            return "Unknown Region";
        }
    }
}