package com.graphic.gameproject;

import java.util.ArrayList;

public class Manager implements Runnable{
    private int level;
    private ArrayList<Enemy> enemies;
    private Director director;

    public Manager() {
        level = 1;
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
}
