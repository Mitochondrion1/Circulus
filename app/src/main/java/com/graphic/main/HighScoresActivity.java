package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_high_scores);

        TextView highestLevelValue = findViewById(R.id.highestLevelValue);
        highestLevelValue.setText(String.valueOf(Store.readInt(getApplicationContext(), R.string.highest_level_key, 0)));
        TextView mostKillsValue = findViewById(R.id.mostKillsValue);
        mostKillsValue.setText(String.valueOf(Store.readInt(getApplicationContext(), R.string.most_kills_key, 0)));
    }
}