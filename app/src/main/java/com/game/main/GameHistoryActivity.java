package com.game.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.game.main.database.DatabaseHelper;
import com.game.main.database.GameModel;

public class GameHistoryActivity extends AppCompatActivity {
    private ListView lvGames;

    private DatabaseHelper databaseHelper;
    private ArrayAdapter<GameModel> gameArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        lvGames = findViewById(R.id.lvGames);
        showGamesOnListView(databaseHelper);

        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameModel clickedGame = (GameModel)parent.getItemAtPosition(position);
                databaseHelper.deleteOne(clickedGame);
                showGamesOnListView(databaseHelper);
                Toast.makeText(GameHistoryActivity.this, "Successfully deleted game info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showGamesOnListView(DatabaseHelper databaseHelper) {
        gameArrayAdapter = new ArrayAdapter<>(GameHistoryActivity.this, android.R.layout.simple_list_item_1, databaseHelper.getEverything());
        lvGames.setAdapter(gameArrayAdapter);
    }
}