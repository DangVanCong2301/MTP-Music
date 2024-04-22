package com.example.spotifyapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.FavoriteAdapter;
import com.example.spotifyapp.databinding.FragmentFavoriteBinding;
import com.example.spotifyapp.databinding.FragmentHomeBinding;
import com.example.spotifyapp.models.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private  List<Song> listFavourite = new ArrayList<>();

    private FavoriteAdapter favoriteAdapter;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
    }

    private void fetchFavouriteSongs() {
        ValueEventListener fetchFavouriteSongListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFavourite.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song song = dataSnapshot.getValue(Song.class);
                    listFavourite.add(song);
                }
                favoriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database.getReference()
                .child("FavouriteSong")
                .child(Objects.requireNonNull(mAuth.getUid()))
                .child("Songs").addValueEventListener(fetchFavouriteSongListener);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        favoriteAdapter = new FavoriteAdapter(listFavourite,getContext());
        binding.recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recyclerViewFavorite.setAdapter(favoriteAdapter);
        favoriteAdapter.setOnItemClickListener(song -> {
            database.getReference()
                    .child("FavouriteSong")
                    .child(Objects.requireNonNull(mAuth.getUid()))
                    .child("Songs")
                    .child(song.getId())
                    .removeValue().addOnSuccessListener(aVoid -> {
                        listFavourite.remove(song);
                        favoriteAdapter.notifyDataSetChanged();
                    });
        });
        initFirebase();
        fetchFavouriteSongs();
        return binding.getRoot();
    }
}