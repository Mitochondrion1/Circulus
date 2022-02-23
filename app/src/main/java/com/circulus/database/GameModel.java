package com.circulus.database;

/**
 * A class of an object that contains the properties of a game.
 */
public class GameModel {
    /** The id of the game. */
    private int id;
    /** The date the game was played on. */
    private String date;
    /** The length of the game. */
    private String time;
    /** The level the game ended on. */
    private int level;
    /** The amount of kills in the game. */
    private int kills;
    /** The score achieved in the game. */
    private int score;
    /** Is the game marked as favorite. */
    private boolean favorite;

    /**
     * Constructs a game info object.
     * <p>
     * @param id        The id of the game.
     * @param date      The date the game was played on.
     * @param time      The length of the game.
     * @param level     The level the game ended on.
     * @param kills     The amount of kills in the game.
     * @param score     The score achieved in the game.
     * @param favorite  Is the game marked as favorite.
     */
    public GameModel(int id, String date, String time, int level, int kills, int score, boolean favorite) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.level = level;
        this.kills = kills;
        this.score = score;
        this.favorite = favorite;
    }

    /**
     * An empty constructor.
     */
    public GameModel() {
    }

    /**
     * Gets the id of the game.
     * <p>
     * @return The id of the game.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the game.
     * <p>
     * @param id The id of the game.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the level the game ended on.
     * <p>
     * @return The level the game ended on.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level the game ended on.
     * <p>
     * @param level The level the game ended on.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the amount of kills in the game.
     * <p>
     * @return The amount of kills in the game.
     */
    public int getKills() {
        return kills;
    }

    /**
     * Sets the amount of kills in the game.
     * <p>
     * @param kills The amount of kills in the game.
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * Gets the score achieved in the game.
     * <p>
     * @return The score achieved in the game.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score achieved in the game.
     * <p>
     * @param score The score achieved in the game.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the date the game was played on.
     * <p>
     * @return The date the game was played on.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the game was played on.
     * <p>
     * @param date The date the game was played on.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the length of the game.
     * <p>
     * @return The length of the game.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the length of the game.
     * <p>
     * @param time The length of the game.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Is the game marked as favorite or not.
     * <p>
     * @return True if marked as favorite, otherwise false.
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Sets the value for if the game is marked as favorite.
     * <p>
     * @param favorite True if marked as favorite, otherwise false.
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Gets a string that represents the object.
     * <p>
     * @return A string with game info.
     */
    @Override
    public String toString() {
        return "Game #" + id + ":\n" +
                "Played on " + date + '\n' +
                "Length: " + time + "\n" +
                "Level reached: " + level + "\n" +
                "Total kills: " + kills + "\n" +
                "Score: " + score;
    }
}
