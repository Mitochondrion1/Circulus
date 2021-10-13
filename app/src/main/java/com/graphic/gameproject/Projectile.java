package com.graphic.gameproject;

public class Projectile implements Runnable {
    private float damage;
    private Vector2 position;
    private Vector2 velocity;
    private boolean isFromPlayer;
    private boolean hitTarget;
    private Thread thread;

    public Projectile(float damage, Vector2 position, Vector2 velocity, boolean isFromPlayer) {
        this.damage = damage;
        this.position = position;
        this.velocity = velocity;
        this.isFromPlayer = isFromPlayer;
        hitTarget = false;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!hitTarget) {
            this.position.setX(this.position.getX() + this.velocity.getX());
            this.position.setY(this.position.getY() + this.velocity.getY());

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isFromPlayer() {
        return isFromPlayer;
    }

    public float getDamage() {
        return damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isHitTarget() {
        return hitTarget;
    }

    public void setHitTarget(boolean hasHitTarget) {
        this.hitTarget = hasHitTarget;
    }
}
