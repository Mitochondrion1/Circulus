package com.graphic.main;

public abstract class Entity implements Runnable {
    protected Vector2 position;
    protected Vector2 velocity;
    protected float speed;
    protected float health;
    protected float maxHealth;
    protected float damage;
    protected float size;
    protected Thread thread;

    public Entity(Vector2 position, float speed, float maxHealth, float damage) {
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.damage = damage;

        this.thread = new Thread(this);
        this.thread.start();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void damage(float damage) {
        this.health -= damage;
    }

    public boolean detectHit(Projectile projectile) {
        return false;
    }

    protected void behave() {
    }
}
