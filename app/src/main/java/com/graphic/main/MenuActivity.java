package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// The menu activity
public class MenuActivity extends AppCompatActivity {
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu);
        language = Store.readString(getApplicationContext(), R.string.language_key, "en");
        if (language.equals("he")) {
            ((Button)findViewById(R.id.play_button)).setText(R.string.button_play_he);
            ((Button)findViewById(R.id.settings_button)).setText(R.string.button_settings_he);
            ((Button)findViewById(R.id.credits_button)).setText(R.string.button_credits_he);
            ((Button)findViewById(R.id.high_scores_button)).setText(R.string.high_scores_text_he);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        language = Store.readString(getApplicationContext(), R.string.language_key, "en");
        if (language.equals("he")) {
            ((Button) findViewById(R.id.play_button)).setText(R.string.button_play_he);
            ((Button) findViewById(R.id.settings_button)).setText(R.string.button_settings_he);
            ((Button) findViewById(R.id.credits_button)).setText(R.string.button_credits_he);
            ((Button) findViewById(R.id.high_scores_button)).setText(R.string.high_scores_text_he);
        }
    }

    // Start game (go to MainActivity)
    public void play(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Open game settings (go to SettingsActivity)
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Show game credits (go to CreditsActivity)
    public void showCredits(View view) {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    // Show high scores (go to HighScoreActivity)
    public void showHighScores(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }
}