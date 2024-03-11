package com.example.spotifyapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.spotifyapp.databinding.ActivityIntroduceBinding;

public class IntroduceActivity extends BaseActivity {

    private ActivityIntroduceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroduceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initListener();

    }

    private void initListener() {
        binding.btnLoginMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroduceActivity.this, LoginActivity.class));
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroduceActivity.this, MainActivity.class));
            }
        });
    }
}