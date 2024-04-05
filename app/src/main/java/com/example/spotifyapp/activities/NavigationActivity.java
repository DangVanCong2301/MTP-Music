package com.example.spotifyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.ViewPagerAdapter;
import com.example.spotifyapp.databinding.ActivityNavigationBinding;
import com.example.spotifyapp.fragments.DiscoverFragment;
import com.example.spotifyapp.fragments.FavoriteFragment;
import com.example.spotifyapp.fragments.HomeFragment;
import com.example.spotifyapp.fragments.LibraryFragment;
import com.example.spotifyapp.fragments.PersonFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityNavigationBinding binding;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_DISCOVER = 1;
    private static final int FRAGMENT_FAVORITE = 2;
    private static final int FRAGMENT_CHAT = 3;
    private static final int FRAGMENT_ACCOUNT = 4;
    private int mCurrentFragment = FRAGMENT_HOME;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager2.setAdapter(viewPagerAdapter);

        setSupportActionBar(binding.toolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(this);

        // replaceFragment(new HomeFragment());
        binding.navigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    openHomeFragment();
                    // binding.navigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                } else if (id == R.id.nav_discover) {
                    openDiscoverFragment();
                    // binding.navigationView.getMenu().findItem(R.id.navigation_discover).setChecked(true);
                } else if (id == R.id.nav_favorite) {
                    openFavoriteFragment();
                    // binding.navigationView.getMenu().findItem(R.id.navigation_favorite).setChecked(true);
                } else if (id == R.id.nav_library) {
                    openChatFragment();
                    // binding.navigationView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
                } else if (id == R.id.nav_persion) {
                    openAccountFragment();
                    // binding.navigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
                }
                return true;
            }
        });

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mCurrentFragment = FRAGMENT_HOME;
                        binding.navigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        mCurrentFragment = FRAGMENT_DISCOVER;
                        binding.navigationView.getMenu().findItem(R.id.navigation_discover).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_discover).setChecked(true);
                        break;
                    case 2:
                        mCurrentFragment = FRAGMENT_FAVORITE;
                        binding.navigationView.getMenu().findItem(R.id.navigation_favorite).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                        break;
                    case 3:
                        mCurrentFragment = FRAGMENT_CHAT;
                        binding.navigationView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_library).setChecked(true);
                        break;
                    case 4:
                        mCurrentFragment = FRAGMENT_ACCOUNT;
                        binding.navigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_persion).setChecked(true);
                        break;
                }
            }
        });
    }

    private void openHomeFragment() {
        if (mCurrentFragment != FRAGMENT_HOME) {
            // replaceFragment(new HomeFragment());
            binding.viewPager2.setCurrentItem(0);
            mCurrentFragment = FRAGMENT_HOME;
        }
    }

    private void openDiscoverFragment() {
        if (mCurrentFragment != FRAGMENT_DISCOVER) {
            // replaceFragment(new DiscoverFragment());
            binding.viewPager2.setCurrentItem(1);
            mCurrentFragment = FRAGMENT_DISCOVER;
        }
    }

    private void openFavoriteFragment() {
        if (mCurrentFragment != FRAGMENT_FAVORITE) {
            // replaceFragment(new FavoriteFragment());
            binding.viewPager2.setCurrentItem(2);
            mCurrentFragment = FRAGMENT_FAVORITE;
        }
    }

    private void openChatFragment() {
        if (mCurrentFragment != FRAGMENT_CHAT) {
            // replaceFragment(new LibraryFragment());
            binding.viewPager2.setCurrentItem(3);
            mCurrentFragment = FRAGMENT_CHAT;
        }
    }

    private void openAccountFragment() {
        if (mCurrentFragment != FRAGMENT_ACCOUNT) {
            // replaceFragment(new PersonFragment());
            binding.viewPager2.setCurrentItem(4);
            mCurrentFragment = FRAGMENT_ACCOUNT;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_home) {
            openHomeFragment();
            // binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
        } else if (id == R.id.navigation_discover) {
            openDiscoverFragment();
            // binding.bottomNavigation.getMenu().findItem(R.id.nav_discover).setChecked(true);
        } else if (id == R.id.navigation_favorite) {
            openFavoriteFragment();
            // binding.bottomNavigation.getMenu().findItem(R.id.nav_favorite).setChecked(true);
        } else if (id == R.id.navigation_chat) {
            // binding.bottomNavigation.getMenu().findItem(R.id.nav_library).setChecked(true);
            openChatFragment();
        } else if (id == R.id.navigation_account) {
            openAccountFragment();
            // binding.bottomNavigation.getMenu().findItem(R.id.nav_persion).setChecked(true);
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true; // Chuyển để đúng với Fragment nào thì nó sẽ selected đến fragment đó
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen((GravityCompat.START))) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.commit();
//    }
}