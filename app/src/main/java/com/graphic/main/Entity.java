package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class Entity implements Runnable {
    protected Vector2 position;
    protected Vector2 velocity;
    protected float speed;
    protected float health;
    protected float maxHealth;
    protected float damage;
    protected float size;
    protected Paint paint;
    protected Thread thread;
    protected MainView view;

    public Entity(Vector2 position, float speed, float maxHealth, float damage) {
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.damage = damage;
        this.paint = new Paint();

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

    public float getSize() {
        return this.size;
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

    protected void drawHealthBar(Canvas canvas) {
        float healthBarBottom = this.position.getY() - 0.6f * this.size;
        float height = 15f;

        Paint green, red;
        green = new Paint();
        green.setColor(Color.GREEN);
        red = new Paint();
        red.setColor(Color.RED);

        Vector2 bottomLeft = new Vector2(this.position.getX() - this.size / 2, healthBarBottom);
        bottomLeft = view.positionToPixels(view.relativePosition(bottomLeft));
        if (health < maxHealth && health > 0) {
            canvas.drawRect(bottomLeft.getX(), bottomLeft.getY() - 15f,
                    bottomLeft.getX() + (this.health / this.maxHealth) * this.size * view.getPixelsPerUnit(),
                    bottomLeft.getY(), green);
            canvas.drawRect(bottomLeft.getX() + (this.health / this.maxHealth) * this.size * view.getPixelsPerUnit(),
                    bottomLeft.getY() - height, bottomLeft.getX() + this.size * view.getPixelsPerUnit(),
                    bottomLeft.getY(), red);
        }
    }
}
