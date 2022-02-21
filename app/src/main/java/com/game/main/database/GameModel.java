package com.game.main.database;

public class GameModel {
    private int id;
    private String date;
    private String time;
    private int level;
    private int kills;
    private int score;
    private boolean favorite;

    public GameModel(int id, String date, String time, int level, int kills, int score, boolean favorite) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.level = level;
        this.kills = kills;
        this.score = score;
        this.favorite = favorite;
    }

    public GameModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

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
