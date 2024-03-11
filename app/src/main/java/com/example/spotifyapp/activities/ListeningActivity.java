package com.example.spotifyapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spotifyapp.R;
import com.example.spotifyapp.databinding.ActivityListeningBinding;
import com.example.spotifyapp.models.Song;

import java.io.IOException;

public class ListeningActivity extends BaseActivity implements SensorEventListener {

    private ActivityListeningBinding binding;
    private SensorManager sensorManager;
    private AudioManager audioManager;
    private Sensor gravitySensor;
    private Boolean isAvailable;
    private Song object;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private static final String TAG = "LISTEN_SONG";
    private float zAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
        startMusic();
        startAnimation();
        initListener();
        initSensor();

    }

    private void getIntentExtra() {
        object = (Song) getIntent().getSerializableExtra("object");
        Log.d(TAG, "object: " + object.getUrl());
    }

    private void setVariable() {
        binding.sbSong.setMax(100);

        Glide.with(ListeningActivity.this)
                .load(object.getImageUrl())
                .into(binding.imgSong);

        binding.tvSongName.setText(object.getSongName());
        binding.tvSongArtist.setText(object.getSongArtist());

        // Khởi tạo MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // chuẩn bị
        prepareMediaPlayer();
    }

    private void prepareMediaPlayer() {
        try {
            mediaPlayer.setDataSource(object.getUrl());
            mediaPlayer.prepare();
            binding.tvTotalDuration.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
            Log.d(TAG, "setVariable: " + mediaPlayer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String milliSecondsToTimer(long milliSeconds) {
        String timerString = "";
        String secondsString;
        // Chia lấy phần nguyên : /, chia lay phần dư %
        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        timerString = timerString + minutes + ":" + secondsString;
        return timerString;
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            binding.tvCurrentTime.setText(milliSecondsToTimer(currentDuration));
        }
    };

    private void updateSeekbar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            binding.sbSong.setProgress((int)(((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private void startMusic() {
        mediaPlayer.start();
        updateSeekbar();
    }

    private void startAnimation() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                binding.imgSong.animate().rotation(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        binding.imgSong.animate().rotation(360).withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    private void stopAnimation() {
        binding.imgSong.animate().cancel();
    }

    private void initListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                finish();
            }
        });

        binding.btnSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSensorAttackMenu();
            }
        });

        binding.btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    binding.btnPlayPause.setImageResource(R.drawable.baseline_play_circle_outline);
                    stopAnimation();
                } else {
                    mediaPlayer.start();
                    binding.btnPlayPause.setImageResource(R.drawable.baseline_pause_circle_outline);
                    updateSeekbar();
                    startAnimation();
                }
            }
        });

        binding.sbSong.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SeekBar seekBar = (SeekBar) view;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                binding.tvCurrentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                binding.sbSong.setSecondaryProgress(i);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                binding.sbSong.setProgress(0);
                binding.btnPlayPause.setImageResource(R.drawable.baseline_play_circle_outline);
                binding.tvCurrentTime.setText(R.string.zero);
                mediaPlayer.reset();
                prepareMediaPlayer();
            }
        });
    }
    private void initSensor() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            isAvailable = true;
        } else {
            Toast.makeText(this, "Điện thoại bại không có cảm biến trọng lực :(", Toast.LENGTH_SHORT).show();
            isAvailable = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đăng ký cảm biến trọng lực
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Huỷ đăng ký cảm biến trọng lực
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private void showSensorAttackMenu() {
        // Khởi tạo/đặt popup enu
        PopupMenu popupMenu = new PopupMenu(this, binding.btnSensor);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Cảm biến dừng");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Cảm biến chuyển bài");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int which = menuItem.getItemId();
                if (which == 0) {
                    // Cảm biến dừng/chạy bài hát
                    changePlayOrPause();
                } else if (which == 1) {
                    // Cảm biến chuyển bài hát
                }
                return false;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        zAxis = sensorEvent.values[2];
        binding.tvGravity.setText(sensorEvent.values[2] + "m/s2");

        //changePlayOrPause(zAxis);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void changePlayOrPause() {
        Log.d(TAG, "change: " + zAxis);
        if (zAxis < 9) {
            Toast.makeText(this, "Thay đổi của trục Z: " + zAxis, Toast.LENGTH_SHORT).show();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                binding.btnPlayPause.setImageResource(R.drawable.baseline_play_circle_outline);
            }
        } else {
            mediaPlayer.start();
            binding.btnPlayPause.setImageResource(R.drawable.baseline_pause_circle_outline);
            updateSeekbar();
        }
    }
}