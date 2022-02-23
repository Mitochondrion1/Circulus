package com.circulus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that defines an object used for saving game info into an SQLite database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /** Represents the name of the table. */
    public static final String GAMES_TABLE = "GAMES_TABLE";
    /** Represent the id column. */
    public static final String COLUMN_ID = "ID";
    /** Represents the date column. */
    public static final String COLUMN_DATE = "DATE";
    /** Represents the game length column. */
    public static final String COLUMN_TIME = "TIME";
    /** Represents the game level column. */
    public static final String COLUMN_GAME_LEVEL = "GAME_LEVEL";
    /** Represents the game kill amount column. */
    public static final String COLUMN_GAME_KILLS = "GAME_KILLS";
    /** Represents the game score column. */
    public static final String COLUMN_GAME_SCORE = "GAME_SCORE";
    /** Represents the favorite game column. */
    public static final String COLUMN_FAVORITE = "FAVORITE";

    /**
     * Constructs a database helper object.
     * <p>
     * @param context The context of the database.
     */
    public DatabaseHelper(Context context) {
        super(context, "games.db", null, 1);
    }

    /**
     * Creates the database table once the helper object is created.
     * <p>
     * @param db The database.
     */
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

    /**
     * Adds a game to the game info database.
     * <p>
     * @param gameModel The object that contains game info.
     * @return          True if the game info has been successfully added to the database, otherwise false.
     */
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

    /**
     * Get a list of games from the database
     * <p>
     * @param mode  What games to retrieve: 1 - favorites; otherwise - all.
     * @return      A list of games retrieved according to the given value.
     */
    public List<GameModel> getGames(int mode) {
        List<GameModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + GAMES_TABLE;
        if (mode == 1) {
            queryString = queryString + " WHERE " + COLUMN_FAVORITE + " = 1";
        }

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

    /**
     * Delete a game with the id of a given game from the database.
     * <p>
     * @param gameModel The game to remove.
     * @return          True if the game was removed from the database, otherwise false.
     */
    public boolean deleteOne(GameModel gameModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + GAMES_TABLE + " WHERE " + COLUMN_ID + " = " + gameModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        return cursor.moveToFirst();
    }

    /**
     * Toggles the favorite value of a given game.
     * <p>
     * @param gameModel The game to toggle favorite for.
     * @return          True if successful, otherwise false.
     */
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
