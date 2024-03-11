package com.example.spotifyapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.BannerSongAdapter;
import com.example.spotifyapp.adapters.CategoryAdapter;
import com.example.spotifyapp.databinding.FragmentHomeBinding;
import com.example.spotifyapp.models.Category;
import com.example.spotifyapp.models.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private View mView;
    private ArrayList<Song> list;
    private ArrayList<Category> categoryArrayList;
    private BannerSongAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private static final String TAG = "HOME_FRAG";
    private final Handler mHandlerBanner = new Handler(Looper.getMainLooper());

    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (list == null || list.isEmpty()) {
                return;
            }
            if (binding.viewPager2.getCurrentItem() == list.size() - 1) {
                binding.viewPager2.setCurrentItem(0);
                return;
            }
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        initFirebase();
        setBannerSong();
        loadCategories();

        mView = binding.getRoot();
        return mView;
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void setBannerSong() {
        list = new ArrayList<>();
        DatabaseReference ref = database.getReference("Songs");
        Query query = ref.orderByChild("isBest").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.d(TAG, "data: " + ds);
                        Song song = ds.getValue(Song.class);
                        list.add(song);
                    }
                    Log.d(TAG, "setBannerSong: " + list);
                    adapter = new BannerSongAdapter(getContext(), list);
                    binding.viewPager2.setAdapter(adapter);
                    binding.indicator3.setViewPager(binding.viewPager2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    private void loadCategories() {
        categoryArrayList = new ArrayList<>();
        binding.prgListCategory.setVisibility(View.VISIBLE);
        DatabaseReference ref = database.getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.d(TAG, "ds: " + ds);
                        Category category = ds.getValue(Category.class);
                        Log.d(TAG, "onDataChange: " + category);
                        categoryArrayList.add(category);
                    }
                    if (categoryArrayList.size() > 0) {
                        binding.rcvCategory.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                        categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
                        binding.rcvCategory.setAdapter(categoryAdapter);
                        binding.prgListCategory.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}