package ru.nishty.aai_referee.ui.secretary.players_list.region_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nishty.aai_referee.databinding.RegionItemBinding;
import ru.nishty.aai_referee.entity.secretary.Region;

import java.util.List;

public class RegionsAdapter extends RecyclerView.Adapter<RegionsAdapter.ViewHolder> {

    private final List<Region> regionsList;

    public RegionsAdapter(List<Region> regionsList) {
        this.regionsList = regionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RegionItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Region region = regionsList.get(position);
        holder.bind(region);
    }

    @Override
    public int getItemCount() {
        return regionsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;


        public ViewHolder(RegionItemBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvRegionName;
        }

        public void bind(Region region) {
            tvName.setText(region.getName());

        }
    }
}