package com.example.spotifyapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spotifyapp.fragments.DiscoverFragment;
import com.example.spotifyapp.fragments.FavoriteFragment;
import com.example.spotifyapp.fragments.HomeFragment;
import com.example.spotifyapp.fragments.LibraryFragment;
import com.example.spotifyapp.fragments.PersonFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new DiscoverFragment();
            case 2:
                return new FavoriteFragment();
            case 3:
                return new LibraryFragment();
            case 4:
                return new PersonFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
