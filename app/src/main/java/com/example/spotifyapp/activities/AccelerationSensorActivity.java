package com.example.spotifyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.admin.ViewPageListenAdapter;
import com.example.spotifyapp.databinding.ActivityAccelerationSensorBinding;
import com.example.spotifyapp.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class AccelerationSensorActivity extends BaseActivity implements SensorEventListener {
    private ActivityAccelerationSensorBinding binding;
    private SensorManager sensorManager;
    private Sensor accelerateSensor;
    private boolean isAvailable, itIsNotFirstTime = false;
    private float xCurrent, yCurrent, zCurrent, xLast, yLast, zLast;
    private float xDifferent, yDifferent, zDifferent;
    private float shakeThresHold = 5f;
    private Vibrator vibrator; // Rung khi đạt ngưỡng
    private static final String TAG = "ACCELERATION_SONG";
    private Song object;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private long songCount;
    private int songIndex = 1;
    private String audioUrl;
    private boolean isDirty = false;
    private String sendObjectUrl;

    public String getSendObjectUrl() {
        return sendObjectUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccelerationSensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initViewPager2();
        initListenerBtn();
        initSensor();

    }

    private void getIntentExtra() {
        object = (Song) getIntent().getSerializableExtra("object");
        Log.d("cong", "send_data_to_fragment: " + object.getUrl());
        sendObjectUrl = object.getUrl();
    }

    private void initViewPager2() {
        ViewPageListenAdapter adapter = new ViewPageListenAdapter(this);
        binding.viewPageListen.setAdapter(adapter);
        binding.citListen.setViewPager(binding.viewPageListen);
        binding.viewPageListen.setCurrentItem(1);
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

    private void initListenerBtn() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                finish();
            }
        });
    }

    private void initListener() {
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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAvailable = true;
        } else {
            Toast.makeText(this, "Đện thoại của bạn không hỗ trợ cảm biến này", Toast.LENGTH_SHORT).show();
            isAvailable = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAvailable) {
            sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAvailable) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        binding.tvAccelerometer.setText(sensorEvent.values[2] + "m/s2");

        xCurrent = sensorEvent.values[0];
        yCurrent = sensorEvent.values[1];
        zCurrent = sensorEvent.values[2];

        // Tính gia tốc tổng cộng
        double acceleration = Math.sqrt(xCurrent * xCurrent + yCurrent * yCurrent + zCurrent * zCurrent);

        if (itIsNotFirstTime) {
            xDifferent = Math.abs(xLast - xCurrent); // Lấy giá trị tuyệt đối
            yDifferent = Math.abs(yLast - yCurrent);
            zDifferent = Math.abs(zLast - zCurrent);
            // So sánh 2 trong 3 trục lớn hơn 5f thì sẽ xảy ra sự kiện cảm biến
            if (
                    (xDifferent > shakeThresHold && yDifferent > shakeThresHold) ||
                    (xDifferent > shakeThresHold && zDifferent > shakeThresHold) ||
                    (yDifferent > shakeThresHold && zDifferent > shakeThresHold)
            ) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    Toast.makeText(this, "Đang thực hiện cảm biến gia tốc", Toast.LENGTH_SHORT).show();
                } else {
                    vibrator.vibrate(500);
                }
            }
        }
        xLast = xCurrent;
        yLast = yCurrent;
        zLast = zCurrent;
        itIsNotFirstTime = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    private void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updateSeekbar();
        }
    }
}