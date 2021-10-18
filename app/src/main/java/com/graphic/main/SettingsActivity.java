package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar headsetVolume, speakerVolume;
    private Switch accSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);
        headsetVolume = findViewById(R.id.headsetVol);
        speakerVolume = findViewById(R.id.speakerVol);
        accSwitch = findViewById(R.id.accelerometer_switch);
        headsetVolume.setMax(100);
        speakerVolume.setMax(100);
        headsetVolume.setProgress(Store.readInt(getApplicationContext(), R.string.headset_volume_key, 20));
        speakerVolume.setProgress(Store.readInt(getApplicationContext(), R.string.speaker_volume_key, 100));
        accSwitch.setChecked(Store.readBool(getApplicationContext(), R.string.accelerometer_key, true));
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
        Store.saveInt(getApplicationContext(), R.string.headset_volume_key, headsetVolume.getProgress());
        Store.saveInt(getApplicationContext(), R.string.speaker_volume_key, speakerVolume.getProgress());
        Store.saveBool(getApplicationContext(), R.string.accelerometer_key, accSwitch.isChecked());
    }
}