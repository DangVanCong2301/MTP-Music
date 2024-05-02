package com.example.spotifyapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.spotifyapp.databinding.ItemFavoriteSongBinding;
import com.example.spotifyapp.interfaces.IDeleteFavoriteSongListener;
import com.example.spotifyapp.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteSongAdapter extends RecyclerView.Adapter<FavoriteSongAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Song> items;
    private IDeleteFavoriteSongListener iDeleteFavoriteSongListener;
    private ItemFavoriteSongBinding binding;

    public FavoriteSongAdapter(Context context, ArrayList<Song> items, IDeleteFavoriteSongListener iDeleteFavoriteSongListener) {
        this.context = context;
        this.items = items;
        this.iDeleteFavoriteSongListener = iDeleteFavoriteSongListener;
    }

    @NonNull
    @Override
    public FavoriteSongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = ItemFavoriteSongBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteSongAdapter.ViewHolder holder, int position) {
        Song song = items.get(position);
        if (song == null) {
            return;
        }

        loadFavoriteSongDetails(song, holder);

        holder.binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                iDeleteFavoriteSongListener.deleteFavoriteSong();
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
        private ItemFavoriteSongBinding binding;
        public ViewHolder(@NonNull ItemFavoriteSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void loadFavoriteSongDetails(Song song, ViewHolder holder) {
        String songId = song.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Songs");
        ref.child(songId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Lấy thông tin các bài hát
                        String songName = "" + snapshot.child("songName").getValue(); // Lây giá trị : getValue();
                        String songArtist = "" + snapshot.child("songArtist").getValue();
                        String songImage = "" + snapshot.child("imageUrl").getValue();

                        Log.d("favorite_song", "songImage: " + songImage);

                        // Đặt lại dữ liệu
                        song.setSongName(songName);
                        song.setSongArtist(songArtist);
                        song.setImageUrl(songImage);

                        // Đặt dữ liệu cho view
                        holder.binding.tvSongName.setText(songName);
                        holder.binding.tvSongArtist.setText(songArtist);

                        Glide.with(context)
                                .load(songImage)
                                .transform(new CenterCrop(), new RoundedCorners(10))
                                .into(holder.binding.imgSong);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
