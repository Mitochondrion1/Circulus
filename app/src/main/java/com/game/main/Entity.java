package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Defines dynamic object, with health and damage
 */
public abstract class Entity implements Runnable, Drawable {
    /** The base health of the entity. */
    protected float baseHealth;
    /** The base damage of the entity. */
    protected float baseDamage;

    /** The position of the entity. */
    protected Vector2 position;
    /** The pixel position of the entity (position on screen). */
    protected Vector2 pixelPosition;
    /** The velocity of the entity (has direction). */
    protected Vector2 velocity;
    /** The speed of the entity (scalar). */
    protected float speed;
    /** The current health of the entity. */
    protected float health;
    /** The maximum health of the entity. */
    protected float maxHealth;
    /** The real damage of the entity. */
    protected float damage;
    /** The diameter of the entity. */
    protected float size;
    /** The wait time between executions of code on the thread. */
    protected long waitTime;
    /** The paint of the entity. */
    protected Paint paint;
    /** The thread of the entity. */
    protected Thread thread;
    /** The main game view. */
    protected MainView view;
    /** The manager object associated with the entity. */
    protected Manager manager;

    /**
     * Constructor
     * <p>
     * @param position  The position of the entity.
     * @param view      The main game view.
     */
    public Entity(Vector2 position, MainView view) {
        this.position = position;
        this.view = view;
        this.velocity = new Vector2(0, 0);
        this.paint = new Paint();
        this.waitTime = 15;

        this.thread = new Thread(this);
    }

    /**
     * Code executed by the thread.
     */
    @Override
    public void run() {
        behave();
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the position of the entity.
     * <p>
     * @return The position of the entity.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set a new position for the entity.
     * <p>
     * @param position The new position.
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Get the size of the entity.
     * <p>
     * @return The size (diameter) of the entity.
     */
    public float getSize() {
        return this.size;
    }

    /**
     * Get the current health of the entity.
     * <p>
     * @return Entity's current health.
     */
    public float getHealth() {
        return health;
    }

    /**
     * Damage the entity.
     * <p>
     * @param damage Amount of damage to do to the entity.
     */
    public void damage(float damage) {
        this.health -= damage;
    }

    /**
     * Detect a collision of a projectile with the entity.
     * <p>
     * @param projectile    The projectile to check collision with.
     * @return              True if there is a collision, otherwise false.
     */
    public boolean detectHit(Projectile projectile) {
        return Vector2.distance(this.position, projectile.getPosition()) < this.size / 2 + projectile.getSize() / 2;
    }

    /**
     * Returns whether the entity is alive or not.
     * <p>
     * @return True if the entity is alive, otherwise false.
     */
    public boolean isAlive() {
        return this.health > 0;
    }

    /**
     * A method that defines the behavior of the entity.
     */
    protected void behave() {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(pixelPosition.getX(), pixelPosition.getY(),
                this.view.getPixelsPerUnit() * this.size / 2, paint);
        drawHealthBar(canvas);
    }

    /**
     * Draw the health bar of the entity.
     * <p>
     * @param canvas The canvas used to draw.
     */
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
