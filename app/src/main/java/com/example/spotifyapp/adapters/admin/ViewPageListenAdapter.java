package com.example.spotifyapp.adapters.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spotifyapp.fragments.listen.ListenFragment;
import com.example.spotifyapp.fragments.listen.LyricsFragment;
import com.example.spotifyapp.fragments.listen.SongInfoFragment;

public class ViewPageListenAdapter extends FragmentStateAdapter {
    public ViewPageListenAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SongInfoFragment();
            case 1:
                return new ListenFragment();
            case 2:
                return new LyricsFragment();
            default:
                return new ListenFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
