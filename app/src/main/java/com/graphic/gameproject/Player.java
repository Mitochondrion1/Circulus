package com.graphic.gameproject;

public class Player implements Runnable {
    private float health;
    private Vector2 position;
    private Vector2 velocity;
    private Thread thread;

    public Player(float health, Vector2 position) {
        this.health = health;
        this.position = position;
        velocity = new Vector2(0, 0);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (health > 0) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
