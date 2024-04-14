package com.example.spotifyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.spotifyapp.BaseActivity;
import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.ViewPagerAdapter;
import com.example.spotifyapp.databinding.ActivityMainBinding;
import com.example.spotifyapp.models.Song;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    private Song mSong;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_DISCOVER = 1;
    private static final int FRAGMENT_FAVORITE = 2;
    private static final int FRAGMENT_CHAT = 3;
    private static final int FRAGMENT_ACCOUNT = 4;
    private int mCurrentPage = FRAGMENT_HOME;
    private static final String TAG = "MAIN_ACTIVITY";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            mSong = (Song) bundle.get("object");

            handleLayoutMusic();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBroadcastReceiver();
        initViewPager2();
        initActionBar();
        initNavigation();
        initBottomNavigation();


    }

    private void initBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
    }

    private void initActionBar() {
        setSupportActionBar(binding.toolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigation() {
        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.navigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    private void initViewPager2() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager2.setAdapter(viewPagerAdapter);
        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setOffscreenPageLimit(4);

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mCurrentPage = FRAGMENT_HOME;
                        binding.navigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        mCurrentPage = FRAGMENT_DISCOVER;
                        binding.navigationView.getMenu().findItem(R.id.navigation_discover).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_discover).setChecked(true);
                        break;
                    case 2:
                        mCurrentPage = FRAGMENT_FAVORITE;
                        binding.navigationView.getMenu().findItem(R.id.navigation_favorite).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_library).setChecked(true);
                        break;
                    case 3:
                        mCurrentPage = FRAGMENT_CHAT;
                        binding.navigationView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                        break;
                    case 4:
                        mCurrentPage = FRAGMENT_ACCOUNT;
                        binding.navigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_persion).setChecked(true);
                        break;
                }
            }
        });
    }

    private void initBottomNavigation() {
        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                binding.viewPager2.setCurrentItem(0);
            } else if (id == R.id.nav_discover) {
                binding.viewPager2.setCurrentItem(1);
            } else if (id == R.id.nav_library) {
                binding.viewPager2.setCurrentItem(2);
            } else if (id == R.id.nav_favorite) {
                binding.viewPager2.setCurrentItem(3);
            } else if (id == R.id.nav_persion) {
                binding.viewPager2.setCurrentItem(4);
            }
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void handleLayoutMusic() {
        Log.d("cong", "handleLayoutMusic: " + mSong.getSongName());
        binding.layoutBottom.setVisibility(View.VISIBLE);
        showInfoSong();
    }

    private void showInfoSong() {
        if (mSong == null) {
            return;
        }
        Glide.with(this)
                .load(mSong.getImageUrl())
                .into(binding.imgSongCustom);
        binding.tvTitleSong.setText(mSong.getSongName());
        binding.tvSingleSong.setText(mSong.getSongArtist());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_home) {
            openHomeFragment();
        } else if (id == R.id.navigation_discover) {
            openDiscoverFragment();
        } else if (id == R.id.navigation_favorite) {
            openFavoriteFragment();
        } else if (id == R.id.navigation_chat) {
            openChatFragment();
        } else if (id == R.id.navigation_account) {
            openAccountFragment();
        }
        return true;
    }

    private void openHomeFragment() {
        if (mCurrentPage != FRAGMENT_HOME) {
            binding.viewPager2.setCurrentItem(0);
            mCurrentPage = FRAGMENT_HOME;
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void openDiscoverFragment() {
        if (mCurrentPage != FRAGMENT_DISCOVER) {
            binding.viewPager2.setCurrentItem(1);
            mCurrentPage = FRAGMENT_DISCOVER;
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void openFavoriteFragment() {
        if (mCurrentPage != FRAGMENT_FAVORITE) {
            binding.viewPager2.setCurrentItem(2);
            mCurrentPage = FRAGMENT_FAVORITE;
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void openChatFragment() {
        if (mCurrentPage != FRAGMENT_CHAT) {
            binding.viewPager2.setCurrentItem(3);
            mCurrentPage = FRAGMENT_CHAT;
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void openAccountFragment() {
        if (mCurrentPage != FRAGMENT_ACCOUNT) {
            binding.viewPager2.setCurrentItem(4);
            mCurrentPage = FRAGMENT_ACCOUNT;
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}