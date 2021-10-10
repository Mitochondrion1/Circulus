package com.graphic.gameproject;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Manager implements Runnable{
    private int level;
    private Player player;
    private ArrayList<Enemy> enemies;
    private Director director;

    public Manager(MainView view) {
        level = 1;
        player = new Player(100f, new Vector2(0f, 0f), view);
        enemies = new ArrayList<Enemy>();
    }

    @Override
    public void run() {
        while (true) {
            // Generate enemies using the director

            // While enemies exist, continue running the level
            while (!enemies.isEmpty()) {
                for (int i = 0; i < enemies.size(); i++) {
                    if (!enemies.get(i).isAlive()) {
                        enemies.remove(i);
                    }
                }
            }

            // Show prompt to continue to next level
        }
    }

    public void moveEnemies(Vector2 positionChange) {
        for (int i = 0; i < this.enemies.size(); i++) {
            this.enemies.get(i).changePosition(positionChange);
        }
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void drawEnemies(Canvas canvas) {
        for (int i = 0; i < this.enemies.size(); i++) {
            this.enemies.get(i).draw(canvas);
        }
    }

    public Player getPlayer() {
        return this.player;
    }
}
