package com.game.main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String GAMES_TABLE = "GAMES_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_GAME_LEVEL = "GAME_LEVEL";
    public static final String COLUMN_GAME_KILLS = "GAME_KILLS";
    public static final String COLUMN_GAME_SCORE = "GAME_SCORE";
    public static final String COLUMN_FAVORITE = "FAVORITE";

    public DatabaseHelper(Context context) {
        super(context, "games.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + GAMES_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_GAME_LEVEL + " INT, " +
                COLUMN_GAME_KILLS + " INT, " +
                COLUMN_GAME_SCORE + " INT, " +
                COLUMN_FAVORITE + " BOOL)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addOne(GameModel gameModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, gameModel.getDate());
        cv.put(COLUMN_TIME, gameModel.getTime());
        cv.put(COLUMN_GAME_LEVEL, gameModel.getLevel());
        cv.put(COLUMN_GAME_KILLS, gameModel.getKills());
        cv.put(COLUMN_GAME_SCORE, gameModel.getScore());
        cv.put(COLUMN_FAVORITE, false);

        long insert = db.insert(GAMES_TABLE, null, cv);

        db.close();

        return insert != -1;
    }

    public List<GameModel> getEverything() {
        List<GameModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + GAMES_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        int gameId, gameLevel, gameKills, gameScore;
        boolean favorite;
        String gameDate, gameTime;
        GameModel model;

        if (cursor.moveToLast()) {
            do {
                gameId = cursor.getInt(0);
                gameDate = cursor.getString(1);
                gameTime = cursor.getString(2);
                gameLevel = cursor.getInt(3);
                gameKills = cursor.getInt(4);
                gameScore = cursor.getInt(5);
                favorite = cursor.getInt(6) == 1;

                model = new GameModel(gameId, gameDate, gameTime, gameLevel, gameKills, gameScore, favorite);
                returnList.add(model);
            } while (cursor.moveToPrevious());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public List<GameModel> getFavorites() {
        List<GameModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + GAMES_TABLE + " WHERE " + COLUMN_FAVORITE + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        int gameId, gameLevel, gameKills, gameScore;
        boolean favorite;
        String gameDate, gameTime;
        GameModel model;

        if (cursor.moveToLast()) {
            do {
                gameId = cursor.getInt(0);
                gameDate = cursor.getString(1);
                gameTime = cursor.getString(2);
                gameLevel = cursor.getInt(3);
                gameKills = cursor.getInt(4);
                gameScore = cursor.getInt(5);
                favorite = cursor.getInt(6) == 1;

                model = new GameModel(gameId, gameDate, gameTime, gameLevel, gameKills, gameScore, favorite);
                if (favorite) {
                    returnList.add(model);
                    Log.d("Added", "yes");
                }
            } while (cursor.moveToPrevious());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public boolean deleteOne(GameModel gameModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + GAMES_TABLE + " WHERE " + COLUMN_ID + " = " + gameModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        db.close();

        return cursor.moveToFirst();
    }

    public boolean toggleFavorite(GameModel gameModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        gameModel.setFavorite(!gameModel.isFavorite());
        cv.put(COLUMN_FAVORITE, gameModel.isFavorite());

        int success = db.update(GAMES_TABLE, cv, COLUMN_ID + " = " + gameModel.getId(), null);

        db.close();

        return success != -1;
    }
}
