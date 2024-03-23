package ru.nishty.aai_referee.ui.secretary.players_list.player_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.databinding.PlayerItemBinding;
import ru.nishty.aai_referee.entity.secretary.Player;

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


        public ViewHolder(PlayerItemBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvPlayerName;
            tvGrade = binding.tvGrade;
            tvPlayerCategory = binding.tvPlayerCategory;
            tvPlayerRegion = binding.tvPlayerRegion;


        }

        public void bind(Player player) {
            tvName.setText(player.getName());
            tvGrade.setText(player.getGrade());
            tvPlayerCategory.setText(player.getCategoryId());;
            tvPlayerRegion.setText(player.getRegionId());

        }
    }
}