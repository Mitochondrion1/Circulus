package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar headsetVolume, speakerVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);
        headsetVolume = findViewById(R.id.headsetVol);
        speakerVolume = findViewById(R.id.speakerVol);
        headsetVolume.setMax(100);
        speakerVolume.setMax(100);
        headsetVolume.setProgress(Store.read(getApplicationContext(), R.string.headset_volume_key, 20));
        speakerVolume.setProgress(Store.read(getApplicationContext(), R.string.speaker_volume_key, 100));
    }


    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }


    private void saveSettings() {
        Store.save(getApplicationContext(), R.string.headset_volume_key, headsetVolume.getProgress());
        Store.save(getApplicationContext(), R.string.speaker_volume_key, speakerVolume.getProgress());
    }
}