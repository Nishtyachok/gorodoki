package ru.nishty.aai_referee.ui.secretary.players_list.region_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.secretary.Region;

import java.util.List;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {

    private List<Region> regionList;

    public RegionAdapter(List<Region> regionList) {
        this.regionList = regionList;
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.region_item, parent, false);
        return new RegionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        Region region = regionList.get(position);
        holder.bind(region);
    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    static class RegionViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;

        public RegionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }

        public void bind(Region region) {
            textViewName.setText(region.getName());
        }
    }
}


