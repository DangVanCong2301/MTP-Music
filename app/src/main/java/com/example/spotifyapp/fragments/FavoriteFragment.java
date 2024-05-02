package com.example.spotifyapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.FavoriteSongAdapter;
import com.example.spotifyapp.databinding.FragmentFavoriteBinding;
import com.example.spotifyapp.interfaces.IDeleteFavoriteSongListener;
import com.example.spotifyapp.models.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;
    private ArrayList<Song> list;
    private FavoriteSongAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        initFirebase();
        loadFavoriteSongs();

        return binding.getRoot();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void loadFavoriteSongs() {
        list = new ArrayList<>();
        binding.prgListFavoriteSong.setVisibility(View.VISIBLE);
        DatabaseReference ref = database.getReference("Users");
        ref.child(mAuth.getUid()).child("Favorites")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Log.d("favorite_song", "onDataChange: " + ds);
                                String songId = "" + ds.child("songId").getValue();
                                Song song = new Song();
                                song.setId(songId);

                                // Thêm vào list danh sách
                                list.add(song);
                            }
                            adapter = new FavoriteSongAdapter(getActivity(), list, new IDeleteFavoriteSongListener() {
                                @Override
                                public void deleteFavoriteSong() {
                                    delete();
                                }
                            });
                            binding.rcvFavoriteSong.setAdapter(adapter);
                            binding.prgListFavoriteSong.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void delete() {
        // Các lựa chọn
        String[] options = { "Thoát", "Xoá" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Bạn có chắc muốn xoá?")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            builder.setCancelable(true);
                        }
                    }
                })
                .show(); // Phải show
    }
}