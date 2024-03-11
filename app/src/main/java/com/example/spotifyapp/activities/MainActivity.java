package com.example.spotifyapp.activities;

import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.example.spotifyapp.BaseActivity;
import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.ViewPagerAdapter;
import com.example.spotifyapp.databinding.ActivityMainBinding;
import com.example.spotifyapp.models.Song;

import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    List<Song> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //list = getListBannerSong();
        Log.d("List", "onCreate: " + list);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager2.setAdapter(viewPagerAdapter);
        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setOffscreenPageLimit(4);

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        binding.tvTitle.setText("Trang chủ");
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_discover).setChecked(true);
                        binding.tvTitle.setText("Khám phá");
                        break;
                    case 2:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_library).setChecked(true);
                        binding.tvTitle.setText("Thư viện");
                        break;
                    case 3:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                        binding.tvTitle.setText("Yêu thích");
                        break;
                    case 4:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_persion).setChecked(true);
                        binding.tvTitle.setText("Cá nhân");
                        break;
                }
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                binding.viewPager2.setCurrentItem(0);
                binding.tvTitle.setText("Trang chủ");
            } else if (id == R.id.nav_discover) {
                binding.viewPager2.setCurrentItem(1);
                binding.tvTitle.setText("Khám phá");
            } else if (id == R.id.nav_library) {
                binding.viewPager2.setCurrentItem(2);
                binding.tvTitle.setText("Thư viện");
            } else if (id == R.id.nav_favorite) {
                binding.viewPager2.setCurrentItem(3);
                binding.tvTitle.setText("Yêu thích");
            } else if (id == R.id.nav_persion) {
                binding.viewPager2.setCurrentItem(4);
                binding.tvTitle.setText("Cá nhân");
            }
            return true;
        });

    }
}