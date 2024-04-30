package com.example.spotifyapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifyapp.BaseActivity;
import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.IntroduceActivity;
import com.example.spotifyapp.databinding.FragmentPersonBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PersonFragment extends Fragment {

    private FragmentPersonBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(inflater, container, false);

        initListener();
        initFirebaseAuth();
        checkUser();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void initListener() {
        binding.layoutSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                checkUser();
            }
        });
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void checkUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            binding.tvEmail.setText("Không tài khoản");
        } else {
            String email = firebaseUser.getEmail();
            binding.tvEmail.setText(email);
        }
    }
}