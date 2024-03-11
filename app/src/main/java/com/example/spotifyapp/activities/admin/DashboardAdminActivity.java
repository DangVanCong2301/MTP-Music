package com.example.spotifyapp.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.spotifyapp.activities.BaseActivity;
import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.IntroduceActivity;
import com.example.spotifyapp.adapters.admin.AdminCategoryAdapter;
import com.example.spotifyapp.databinding.ActivityDashboardAdminBinding;
import com.example.spotifyapp.models.Category;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends BaseActivity {

    private ActivityDashboardAdminBinding binding;

    private ArrayList<Category> list;
    private AdminCategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initListener();
        checkUser();
        loadCategories();

    }

    private void initListener() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                checkUser();
            }
        });

        binding.btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdminActivity.this, CategoryAddActivity.class));
                finish();
            }
        });

        binding.btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdminActivity.this, SongAddActivity.class));
            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, IntroduceActivity.class));
            finish();
        } else {
            // Lấy email
            String email = firebaseUser.getEmail();
            Log.d("Email", "checkUser: " + email);
            binding.tvUsername.setText(email);
        }
    }

    private void loadCategories() {
        String TAG = "Categories";
        list = new ArrayList<>();
        binding.prgListCategory.setVisibility(View.VISIBLE);
        DatabaseReference ref = database.getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Category category = ds.getValue(Category.class);
                        Log.d(TAG, "onDataChange: " + category);
                        list.add(category);
                    }
                    adapter = new AdminCategoryAdapter(DashboardAdminActivity.this, list);
                    binding.rcvCategory.setAdapter(adapter);
                    binding.prgListCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}