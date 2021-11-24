package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

// The menu activity
public class MenuActivity extends AppCompatActivity {
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // Start game (go to MainActivity)
    public void play(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.play_button));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.play_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_game:
                        MainActivity.setNewGame(true);
                        startActivity(intent);
                        return true;
                    case R.id.load_game:
                        MainActivity.setNewGame(false);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
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