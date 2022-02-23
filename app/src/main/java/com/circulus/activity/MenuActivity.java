package com.circulus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.circulus.main.R;

/**
 * The main menu activity.
 */
public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Start the game.
     * <p>
     * @param view The "Play" button.
     */
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


    /**
     * Open settings.
     * <p>
     * @param view The "Settings" button.
     */
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Show credits.
     * <p>
     * @param view The "Credits" button.
     */
    public void showCredits(View view) {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    /**
     * Show high scores.
     * <p>
     * @param view The "High Scores" button.
     */
    public void showHighScores(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    public void openGamesHistory(View view) {
        Intent intent = new Intent(this, GameHistoryActivity.class);
        startActivity(intent);
    }
}