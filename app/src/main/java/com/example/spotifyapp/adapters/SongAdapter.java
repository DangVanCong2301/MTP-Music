package com.example.spotifyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.spotifyapp.activities.ListeningActivity;
import com.example.spotifyapp.databinding.ItemListSongBinding;
import com.example.spotifyapp.models.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Song> items;

    public SongAdapter(Context context, ArrayList<Song> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemListSongBinding binding = ItemListSongBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        Song song = items.get(position);
        if (song == null) {
            return;
        }
        String songName = song.getSongName();
        String songArtist = song.getSongArtist();
        String songImage = song.getImageUrl();

        holder.binding.tvSongName.setText(songName);
        holder.binding.tvSongArtist.setText(songArtist);
        Glide.with(context)
                .load(songImage)
                .transform(new CenterCrop(), new RoundedCorners(10))
                .into(holder.binding.imgSong);

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
        private ItemListSongBinding binding;
        public ViewHolder(@NonNull ItemListSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
