package ru.nishty.aai_referee.ui.secretary.players_list.player_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.secretary.Player;

import java.util.List;

// PlayerAdapter.java
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> playerList;

    public PlayerAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.bind(player);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewCategory;
        private TextView textViewRegion;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
        }

        public void bind(Player player) {
            textViewName.setText(player.getName());
            textViewCategory.setText("Category: " + player.getCategoryId());
            textViewRegion.setText("Region: " + player.getRegionId());
        }
    }
}


