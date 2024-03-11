package com.example.spotifyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spotifyapp.activities.ListeningActivity;
import com.example.spotifyapp.databinding.ItemBannerSongBinding;
import com.example.spotifyapp.models.Song;

import java.util.ArrayList;

public class BannerSongAdapter extends RecyclerView.Adapter<BannerSongAdapter.ViewHolder> {
    ArrayList<Song> items;
    private Context context;

    public BannerSongAdapter(Context context, ArrayList<Song> items) {
        context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BannerSongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemBannerSongBinding binding = ItemBannerSongBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerSongAdapter.ViewHolder holder, int position) {
        Song song = items.get(position);
        if (song == null) {
            return;
        }
        holder.binding.tvTitle.setText(song.getSongName());
        holder.binding.tvListenCount.setText(String.valueOf(song.getListensCount()));

        Glide.with(context)
                .load(song.getImageUrl())
                .into(holder.binding.imgBanner);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListeningActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBannerSongBinding binding;
        public ViewHolder(@NonNull ItemBannerSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
