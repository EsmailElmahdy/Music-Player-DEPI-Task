// MainActivity.java
package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageButton playStopButton;
    private ImageButton repeatButton;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private boolean isPlaying = false;
    private boolean isRepeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playStopButton = findViewById(R.id.btn_play_stop);
        repeatButton = findViewById(R.id.btn_repeat);
        seekBar = findViewById(R.id.seekBar);

        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        seekBar.setMax(mediaPlayer.getDuration());

        playStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    playStopButton.setImageResource(R.drawable.play);
                    handler.removeCallbacks(updateRunnable);
                } else {
                    mediaPlayer.start();
                    playStopButton.setImageResource(R.drawable.pause);
                    updateSeekBar();
                }
                isPlaying = !isPlaying;
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeat = !isRepeat;
                mediaPlayer.setLooping(isRepeat);
                repeatButton.setImageResource(isRepeat ? R.drawable.repeat : R.drawable.repeating_off);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playStopButton.setImageResource(R.drawable.play);
                isPlaying = false;
                handler.removeCallbacks(updateRunnable);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && isPlaying) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(updateRunnable, 1000);
        }
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateRunnable);
    }
}
