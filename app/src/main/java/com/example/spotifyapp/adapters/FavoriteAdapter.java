package com.example.spotifyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.spotifyapp.activities.AccelerationSensorActivity;
import com.example.spotifyapp.databinding.ItemListFavouriteSongBinding;
import com.example.spotifyapp.models.Song;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;



public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Song> favoriteSongs;
    private  OnFavouriteSongClick listener;
    private final Context context;
    public FavoriteAdapter(List<Song> favoriteSongs,Context context) {
        this.favoriteSongs = favoriteSongs;
        this.context = context;
    }

    public void setOnItemClickListener(OnFavouriteSongClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListFavouriteSongBinding binding = ItemListFavouriteSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = favoriteSongs.get(position);
        holder.binding.tvSongName.setText(song.getSongName());
        holder.binding.tvSongArtist.setText(song.getSongArtist());
        Glide.with(context)
                .load(song.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(10))
                .into(holder.binding.imgSong);

        holder.binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(context, AccelerationSensorActivity.class);
            intent.putExtra("object", favoriteSongs.get(position));
            context.startActivity(intent);
        });
        holder.binding.removeFavouriteBtn.setOnClickListener(
                view -> {
                        if (listener != null) {
                            listener.onClick(song);
                        }
                }
        );
    }

    @Override
    public int getItemCount() {
        if(favoriteSongs != null) {
            return favoriteSongs.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListFavouriteSongBinding binding;

        public ViewHolder(@NonNull ItemListFavouriteSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
