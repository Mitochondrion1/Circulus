package com.graphic.gameproject;

public class Projectile implements Runnable {
    private float damage;
    private Vector2 position;
    private Vector2 velocity;
    private boolean isFromPlayer;
    private boolean hasHitTarget;
    private Thread thread;

    public Projectile(float damage, Vector2 position, Vector2 velocity, boolean isFromPlayer) {
        this.damage = damage;
        this.position = position;
        this.velocity = velocity;
        this.isFromPlayer = isFromPlayer;
        hasHitTarget = false;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!hasHitTarget) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
