package com.example.spotifyapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.DiscoverCategoryAdapter;
import com.example.spotifyapp.databinding.FragmentDiscoverBinding;
import com.example.spotifyapp.models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscoverFragment extends Fragment {
    private FragmentDiscoverBinding binding;
    private ArrayList<Category> categoryArrayList;
    private FirebaseDatabase database;
    private DiscoverCategoryAdapter discoverCategoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        initFirebase();
        loadCategoriesDiscover();

        return binding.getRoot();
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
    }

    private void loadCategoriesDiscover() {
        categoryArrayList = new ArrayList<>();
        binding.prgListCategoryDiscover.setVisibility(View.VISIBLE);
        DatabaseReference ref = database.getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Category category = ds.getValue(Category.class);
                        categoryArrayList.add(category);
                    }
                    if (categoryArrayList.size() > 0) {
                        binding.rcvCategoryDiscover.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        discoverCategoryAdapter = new DiscoverCategoryAdapter(getActivity(), categoryArrayList);
                        binding.rcvCategoryDiscover.setAdapter(discoverCategoryAdapter);
                    }
                    binding.prgListCategoryDiscover.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}