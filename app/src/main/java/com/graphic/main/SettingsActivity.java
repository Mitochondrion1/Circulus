package com.graphic.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

// The settings activity
public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    // Declare settings widgets
    private SeekBar headsetVolume, speakerVolume;
    private Switch accSwitch, dirSwitch;
    private SeekBar accSensitivity;
    private Spinner languageSpinner;

    private String language;

    // A numeric value for the current view: 0 = volume_settings, 1 = accessibility_settings
    private int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.settings_volume);
        currentView = 0;
        language = Store.readString(getApplicationContext(), R.string.language_key, "en");
        if (language.equals("he")) {
            findViewById(R.id.volumeSettingsView).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            ((TextView)findViewById(R.id.volumeTitle)).setText(R.string.volume_text_he);
            ((TextView)findViewById(R.id.headsetVolumeTextView)).setText((R.string.headset_volume_he));
            ((TextView)findViewById(R.id.speakerVolumeTextView)).setText((R.string.speaker_volume_he));
        }
        else {
            findViewById(R.id.volumeSettingsView).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
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
        if (language.equals("he")) {
            menu.getItem(0).setTitle(R.string.volume_text_he);
            menu.getItem(1).setTitle(R.string.accessibility_text_he);
            menu.getItem(2).setTitle(R.string.language_text_he);
            menu.getItem(3).setTitle(R.string.to_menu_he);
        }
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
                    if (language.equals("he")) {
                        findViewById(R.id.volumeSettingsView).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        ((TextView)findViewById(R.id.volumeTitle)).setText(R.string.volume_text_he);
                        ((TextView)findViewById(R.id.headsetVolumeTextView)).setText((R.string.headset_volume_he));
                        ((TextView)findViewById(R.id.speakerVolumeTextView)).setText((R.string.speaker_volume_he));
                    }
                    else {
                        findViewById(R.id.volumeSettingsView).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    }
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
                    if (language.equals("he")) {
                        findViewById(R.id.accessibilitySettingsView).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        ((TextView)findViewById(R.id.accessibilityTitle)).setText(R.string.accessibility_text_he);
                        ((TextView)findViewById(R.id.accelerometerTextView)).setText(R.string.accelerometer_text_he);
                        ((TextView)findViewById(R.id.directionsTextView)).setText(R.string.directions_text_he);
                        ((TextView)findViewById(R.id.accSensitivityTextView)).setText(R.string.acc_sensitivity_text_he);
                    }
                    else {
                        findViewById(R.id.accessibilitySettingsView).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    }
                    accSwitch = findViewById(R.id.accelerometer_switch);
                    dirSwitch = findViewById(R.id.direction_switch);
                    accSensitivity = findViewById(R.id.accSensitivity);
                    accSwitch.setChecked(Store.readBool(getApplicationContext(), R.string.accelerometer_key, true));
                    dirSwitch.setChecked(Store.readBool(getApplicationContext(), R.string.direction_display_key, false));
                    accSensitivity.setMax(3);
                    accSensitivity.setProgress(Store.readInt(getApplicationContext(), R.string.sensitivity_key, 2) - 1);
                }
                return true;
            case R.id.language_option:
                if (currentView != 2) {
                    saveSettings();
                    setContentView(R.layout.settings_language);
                    currentView = 2;
                    languageSpinner = (Spinner)findViewById(R.id.languageSpinner);
                    ArrayAdapter<CharSequence> adapter;
                    if (language.equals("he")) {
                        ((TextView)findViewById(R.id.languageTitle)).setText(R.string.language_text_he);
                        adapter = ArrayAdapter.createFromResource(this,
                                R.array.languages_array_he, android.R.layout.simple_spinner_item);
                    }
                    else {
                        ((TextView)findViewById(R.id.languageTitle)).setText(R.string.language_text);
                        adapter = ArrayAdapter.createFromResource(this,
                                R.array.languages_array, android.R.layout.simple_spinner_item);
                    }
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    languageSpinner.setAdapter(adapter);
                    languageSpinner.setOnItemSelectedListener(this);
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

    // Save settings to shared preferences
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Selected", "item selected");
        switch (position) {
            case 0:
                Store.saveString(getApplicationContext(), R.string.language_key, "en");
                language = "en";
                break;
            case 1:
                Store.saveString(getApplicationContext(), R.string.language_key, "he");
                language = "he";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}