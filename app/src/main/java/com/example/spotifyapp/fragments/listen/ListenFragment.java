package com.example.spotifyapp.fragments.listen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.AccelerationSensorActivity;
import com.example.spotifyapp.databinding.FragmentListenBinding;
import com.example.spotifyapp.models.Song;

public class ListenFragment extends Fragment {
    private FragmentListenBinding binding;
    private Song mSong;
    private AccelerationSensorActivity mAccelerationSensorActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListenBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment

        return binding.getRoot();
    }
}