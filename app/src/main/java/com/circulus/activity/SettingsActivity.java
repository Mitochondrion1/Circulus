package com.circulus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;

import com.circulus.main.R;
import com.circulus.utility.Store;

/**
 * The activity that allows to change settings.
 */
public class SettingsActivity extends AppCompatActivity {
    /** Headset volume slider. */
    private SeekBar headsetVolume;
    /** Speaker volume slider. */
    private SeekBar speakerVolume;
    /** Accelerometer mode switch. */
    private Switch accSwitch;
    /** Direction display switch. */
    private Switch dirSwitch;
    /** Accelerometer sensitivity slider. */
    private SeekBar accSensitivity;

    /** A value representing the current layout: 0 - volume, 1 - accessibility. */
    private int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.settings_volume);
        getSupportActionBar().setTitle(R.string.button_settings);
        currentView = 0;
        headsetVolume = findViewById(R.id.headsetVol);
        speakerVolume = findViewById(R.id.speakerVol);
        headsetVolume.setMax(100);
        speakerVolume.setMax(100);
        headsetVolume.setProgress(Store.readInt(getApplicationContext(), R.string.headset_volume_key, 20));
        speakerVolume.setProgress(Store.readInt(getApplicationContext(), R.string.speaker_volume_key, 100));
    }

    /**
     * Inflates the options menu.
     * <p>
     * @param menu  The menu to inflate.
     * @return      true (has no meaning).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    /**
     * Defines what happens when an options menu item is selected.
     * <p>
     * @param item  The menu item that is selected.
     * @return      Returned value has no meaning.
     */
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
                    accSensitivity = findViewById(R.id.accSensitivity);
                    accSwitch.setChecked(Store.readBool(getApplicationContext(), R.string.accelerometer_key, true));
                    dirSwitch.setChecked(Store.readBool(getApplicationContext(), R.string.direction_display_key, false));
                    accSensitivity.setMax(3);
                    accSensitivity.setProgress(Store.readInt(getApplicationContext(), R.string.sensitivity_key, 2) - 1);
                }
                return true;
            case R.id.to_menu_option:
                finish();
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


    /**
     * Saves the settings.
     */
    private void saveSettings() {
        if (currentView == 0) {
            Store.saveInt(getApplicationContext(), R.string.headset_volume_key, headsetVolume.getProgress());
            Store.saveInt(getApplicationContext(), R.string.speaker_volume_key, speakerVolume.getProgress());
        }
        if (currentView == 1) {
            Store.saveBool(getApplicationContext(), R.string.accelerometer_key, accSwitch.isChecked());
            Store.saveBool(getApplicationContext(), R.string.direction_display_key, dirSwitch.isChecked());
            Store.saveInt(getApplicationContext(), R.string.sensitivity_key, accSensitivity.getProgress() + 1);
        }
    }
}