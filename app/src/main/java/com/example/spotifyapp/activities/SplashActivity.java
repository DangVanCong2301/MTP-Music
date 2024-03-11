package com.example.spotifyapp.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.admin.DashboardAdminActivity;
import com.example.spotifyapp.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;

    private Animation animTop, animBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAnimation();
        setAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        }, 4000);
    }

    private void initAnimation() {
        animTop = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        animBottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
    }

    private void setAnimation() {
        binding.imgLogoApp.setAnimation(animTop);
        binding.tvAppName.setAnimation(animBottom);
    }

    private void checkUser() {
        // lây user hiện tại nếu đã đăng nhập
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(SplashActivity.this, IntroduceActivity.class));
            finish();
        } else {
            // Nếu đã đăng nhập, kiểm tra loại truy cập (user, admin)
            DatabaseReference ref = database.getReference("Users");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userType = "" + snapshot.child("userType").getValue();
                            // Kiểm tra loại quyền truy cập
                            if (userType.equals("user")) {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            } else if (userType.equals("admin")){
                                startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}