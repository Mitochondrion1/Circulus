package com.circulus.activity;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.circulus.main.R;
import com.circulus.database.*;

/**
 * Displays finished games that are stored in a database
 */
public class GameHistoryActivity extends AppCompatActivity {
    /** Toggle to display favorite games. */
    private Switch favoritesSwitch;
    /** The list view that displays games. */
    private ListView lvGames;

    /** The database helper object. */
    private DatabaseHelper databaseHelper;
    /** The array adapter for the list view. */
    private ArrayAdapter<GameModel> gameArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        setTitle(R.string.button_games_history);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        favoritesSwitch = findViewById(R.id.switchFavorites);
        favoritesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showGamesOnListView(databaseHelper);
            }
        });
        lvGames = findViewById(R.id.lvGames);
        showGamesOnListView(databaseHelper);

        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameModel clickedGame = (GameModel)parent.getItemAtPosition(position);

                PopupMenu popup = new PopupMenu(GameHistoryActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.game_item_action_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.favorite_game:
                                databaseHelper.toggleFavorite(clickedGame);
                                if (clickedGame.isFavorite()) {
                                    Toast.makeText(GameHistoryActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(GameHistoryActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.delete_info:
                                databaseHelper.deleteOne(clickedGame);
                                Toast.makeText(GameHistoryActivity.this, "Successfully deleted game info", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        showGamesOnListView(databaseHelper);
                        return true;
                    }
                });
            }
        });
    }

    /**
     * Shows games on the list view.
     * <p>
     * @param databaseHelper The database helper object.
     */
    private void showGamesOnListView(DatabaseHelper databaseHelper) {
        if (favoritesSwitch.isChecked()) {
            gameArrayAdapter = new ArrayAdapter<>(GameHistoryActivity.this,
                    android.R.layout.simple_list_item_1, databaseHelper.getGames(1));
        }
        else {
            gameArrayAdapter = new ArrayAdapter<>(GameHistoryActivity.this,
                    android.R.layout.simple_list_item_1, databaseHelper.getGames(0));
        }
        lvGames.setAdapter(gameArrayAdapter);
    }
}