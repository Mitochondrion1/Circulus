package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Defines a round projectile, moving in a straight line.
 */
public class Projectile implements Runnable, Drawable {
    /** The damage of the projectile. */
    private float damage;
    /** The position of the projectile. */
    private Vector2 position;
    /** The velocity of the projectile (has direction). */
    private Vector2 velocity;
    /** The speed of the projectile (scalar). */
    private float speed;
    /** The diameter of the projectile. */
    private float size;
    /** The pause time between ticks. */
    private int waitTime;
    /** The lifetime of the projectile in ticks. */
    private int lifetime;
    /** True if the projectile has hit a target, otherwise false. */
    private boolean hitTarget;
    /** The projectile paint. */
    private Paint paint;
    /** The main game view. */
    private MainView view;
    /** The thread of the projectile. */
    private Thread thread;

    /**
     * Constructs a projectile.
     * <p>
     * @param damage    The projectile damage.
     * @param position  The projectile position.
     * @param velocity  The projectile velocity.
     * @param view      The main game view.
     */
    public Projectile(float damage, Vector2 position, Vector2 velocity, MainView view) {
        this.damage = damage;
        this.position = new Vector2(position.getX(), position.getY());
        this.velocity = velocity;
        this.size = 0.1f;
        speed = 2f;
        waitTime = 15;
        lifetime = 200;
        this.velocity.setLength(speed);
        hitTarget = false;
        this.view = view;

        this.paint = new Paint();
        paint.setColor(Color.YELLOW);

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Code Executed by the thread.
     */
    @Override
    public void run() {
        while (!hitTarget && lifetime > 0) {
            this.position.setX(this.position.getX() + (waitTime / 1000f) * this.velocity.getX());
            this.position.setY(this.position.getY() + (waitTime / 1000f) * this.velocity.getY());
            lifetime--;

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (lifetime == 0) {
            hitTarget = true;
        }
    }

    /**
     * Get the damage of the projectile.
     * <p>
     * @return Projectile's damage.
     */
    public float getDamage() {
        return damage;
    }

    /**
     * Get the position of the projectile.
     * <p>
     * @return Projectile's position.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Returns whether the projectile has hit a target or not.
     * <p>
     * @return True if the projectile has hit a target or its lifetime has expired, otherwise false.
     */
    public boolean isHitTarget() {
        return hitTarget;
    }

    /**
     * Get the size of the projectile.
     * <p>
     * @return Projectile's size (diameter).
     */
    public float getSize() {
        return size;
    }

    /**
     * Set whether the projectile has hit a target.
     * <p>
     * @param hasHitTarget True if the projectile hit a target, other wise false.
     */
    public void setHitTarget(boolean hasHitTarget) {
        this.hitTarget = hasHitTarget;
    }

    /**
     * Draws the projectile.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(findPixelPosition().getX(), findPixelPosition().getY(),
                this.view.getPixelsPerUnit() * this.size / 2, paint);
    }

    /**
     * Finds the pixel position of the projectile and returns it.
     * <p>
     * @return Projectile's pixel position.
     */
    private Vector2 findPixelPosition() {
        return view.positionToPixels(view.relativePosition(this.position));
    }
}
