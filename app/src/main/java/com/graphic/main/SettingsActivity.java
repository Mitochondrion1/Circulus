package com.graphic.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;

// The settings activity
public class SettingsActivity extends AppCompatActivity {
    // Declare settings widgets
    private SeekBar headsetVolume, speakerVolume;
    private Switch accSwitch, dirSwitch;

    // A numeric value for the current view: 0 = volume_settings, 1 = accessibility_settings
    private int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.settings_volume);
        currentView = 0;
        headsetVolume = findViewById(R.id.headsetVol);
        speakerVolume = findViewById(R.id.speakerVol);
        headsetVolume.setMax(100);
        speakerVolume.setMax(100);
        headsetVolume.setProgress(Store.readInt(getApplicationContext(), R.string.headset_volume_key, 20));
        speakerVolume.setProgress(Store.readInt(getApplicationContext(), R.string.speaker_volume_key, 100));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        switch (item.getItemId()) {
            case R.id.volume_option:
                if (currentView != 0) {
                    saveSettings();
                    setContentView(R.layout.settings_volume);
                    currentView = 0;
                    headsetVolume = findViewById(R.id.headsetVol);
                    speakerVolume = findViewById(R.id.speakerVol);
                    headsetVolume.setMax(100);
                    speakerVolume.setMax(100);
                    headsetVolume.setProgress(Store.readInt(getApplicationContext(), R.string.headset_volume_key, 20));
                    speakerVolume.setProgress(Store.readInt(getApplicationContext(), R.string.speaker_volume_key, 100));
                }
                return true;
            case R.id.accessibility_option:
                if (currentView != 1) {
                    saveSettings();
                    setContentView(R.layout.settings_accessibility);
                    currentView = 1;
                    accSwitch = findViewById(R.id.accelerometer_switch);
                    dirSwitch = findViewById(R.id.direction_switch);
                    accSwitch.setChecked(Store.readBool(getApplicationContext(), R.string.accelerometer_key, true));
                    dirSwitch.setChecked(Store.readBool(getApplicationContext(), R.string.direction_display_key, false));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Save settings when the activity is stopped, destroyed or paused
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

    // Save settings to shared preferences
    private void saveSettings() {
        if (currentView == 0) {
            Store.saveInt(getApplicationContext(), R.string.headset_volume_key, headsetVolume.getProgress());
            Store.saveInt(getApplicationContext(), R.string.speaker_volume_key, speakerVolume.getProgress());
        }
        if (currentView == 1) {
            Store.saveBool(getApplicationContext(), R.string.accelerometer_key, accSwitch.isChecked());
            Store.saveBool(getApplicationContext(), R.string.direction_display_key, dirSwitch.isChecked());
        }
    }
}