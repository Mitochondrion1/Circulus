package com.graphic.gameproject;

public class Enemy implements Runnable {
    protected float health;
    protected float damage;
    protected Vector2 position;
    protected boolean alive;
    protected Manager manager;
    protected Thread thread;

    public Enemy(Manager manager) {
        alive = true;
        this.manager = manager;

        thread = new Thread();
        thread.start();
    }

    @Override
    public void run() {
        while (alive) {
            behave();

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        alive = false;
    }

    protected void behave() {

    }

    public boolean isAlive() {
        return alive;
    }
}
