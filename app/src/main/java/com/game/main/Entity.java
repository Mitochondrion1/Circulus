package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

// Defines dynamic object, with health and damage
public abstract class Entity implements Runnable, Drawable {
    protected float baseHealth;
    protected float baseDamage;

    protected Vector2 position;
    protected Vector2 pixelPosition;
    protected Vector2 velocity;
    protected float speed;
    protected float health;
    protected float maxHealth;
    protected float damage;
    protected float size;
    protected long waitTime;
    protected Paint paint;
    protected Thread thread;
    protected MainView view;

    public Entity(Vector2 position, MainView view) {
        this.position = position;
        this.view = view;
        this.velocity = new Vector2(0, 0);
        this.paint = new Paint();
        this.waitTime = 15;

        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        behave();
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getSize() {
        return this.size;
    }

    public void damage(float damage) {
        this.health -= damage;
    }

    public boolean detectHit(Projectile projectile) {
        return Vector2.distance(this.position, projectile.getPosition()) < this.size / 2 + projectile.getSize() / 2;
    }

    // Returns whether the entity is alive or not
    public boolean isAlive() {
        return this.health > 0;
    }

    // A method that defines the behavior of the entity
    protected void behave() {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(pixelPosition.getX(), pixelPosition.getY(),
                this.view.getPixelsPerUnit() * this.size / 2, paint);
        drawHealthBar(canvas);
    }

    // Draw the health bar of the entity
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
