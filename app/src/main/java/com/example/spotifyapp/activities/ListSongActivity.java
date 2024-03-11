package com.example.spotifyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.SongAdapter;
import com.example.spotifyapp.databinding.ActivityListSongBinding;
import com.example.spotifyapp.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListSongActivity extends BaseActivity {
    private ActivityListSongBinding binding;
    private String categoryID, categoryName;
    private ArrayList<Song> list;
    private SongAdapter adapter;
    private static final String TAG = "LIST_SONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();

    }

    private void getIntentExtra() {
        categoryID = getIntent().getStringExtra("categoryID");
        categoryName = getIntent().getStringExtra("categoryName");
        Log.d(TAG, "getIntentExtra: " + categoryID);

        binding.tvTitleCategory.setText(categoryName);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initList() {
        DatabaseReference ref = database.getReference("Songs");
        binding.prgListSong.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
        Query query;
        query = ref.orderByChild("categoryId").equalTo(categoryID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: " + ds);
                        Song song = ds.getValue(Song.class);
                        list.add(song);
                    }
                    if (list.size() > 0) {
                        Log.d(TAG, "size: " + list.size());
                        adapter = new SongAdapter(ListSongActivity.this, list);
                        binding.rcvListSong.setLayoutManager(new LinearLayoutManager(ListSongActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.rcvListSong.setAdapter(adapter);
                    }
                    binding.prgListSong.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}