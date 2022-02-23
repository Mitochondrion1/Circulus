package com.circulus.gameobject;

import android.graphics.Canvas;

import com.circulus.playtime.MainView;
import com.circulus.playtime.Manager;
import com.circulus.utility.Vector2;

/**
 * An abstract, AI controlled enemy.
 */
public abstract class Enemy extends Entity implements Runnable {
    /** The change in position in a tick. */
    protected Vector2 positionChange;
    /** True if the enemy is alive, otherwise false. */
    protected boolean alive;
    /** The arrow associated with the enemy. */
    protected Arrow arrow;

    /** The amount of points the player gains for killing the enemy. */
    protected int scorePoints;

    /**
     * Constructor
     * <p>
     * @param position  The initial position of the enemy.
     * @param manager   The manager object associated with the enemy.
     * @param view      The view the enemy belongs to (the main game view).
     */
    public Enemy(Vector2 position, Manager manager, MainView view) {
        super(position, view);

        alive = true;
        this.manager = manager;
        this.arrow = new Arrow(this.view);
        this.arrow.setAlpha((int)Math.min(0xff, 0xff / Vector2.distance(
                this.manager.getPlayer().getPosition(), this.position)));
        pixelPosition = this.view.positionToPixels(this.view.relativePosition(this.position));
    }

    /**
     * The code that executes on the thread of the enemy.
     */
    @Override
    public void run() {
        Vector2 arrowDir;
        while (health > 0) {
            this.pixelPosition = this.findPixelPosition();
            arrowDir = Vector2.sub(this.manager.getPlayer().getPosition(), this.position);
            arrowDir.setLength(0.5f);
            arrow.setPosition(view.positionToPixels(view.relativePosition(
                    new Vector2(this.manager.getPlayer().getPosition().getX() + arrowDir.getX(),
                    this.manager.getPlayer().getPosition().getY() + arrowDir.getY()))));
            this.arrow.setAlpha((int)Math.min(0xff, 4 * 0xff / Vector2.distance(
                    this.manager.getPlayer().getPosition(), this.position)));

            super.run();
        }
        alive = false;
    }

    /**
     * Calculates the pixel position of the enemy (the position on screen).
     * <p>
     * @return The pixel position of the enemy.
     */
    protected Vector2 findPixelPosition() {
        return view.positionToPixels(view.relativePosition(position));
    }

    @Override
    public void draw(Canvas canvas) {
        this.arrow.draw(canvas);
        super.draw(canvas);
    }

    /**
     * Get the position of the enemy.
     * <p>
     * @return The position of the enemy.
     */
    public Vector2 getPosition() {
        return this.position;
    }

    /**
     * Get the score gain from the enemy.
     * <p>
     * @return The score gained for killing the enemy.
     */
    public int getScorePoints() {
        return scorePoints;
    }

    /**
     * Starts the thread of the enemy.
     */
    public void startThread() {
        thread.start();
    }

    /**
     * Assigns basic values for an enemy.
     * <p>
     * @param health        The base health of the enemy to assign.
     * @param damage        The base damage of the enemy to assign.
     * @param scorePoints   The score gain for killing the enemy to assign.
     */
    protected void assignBasicValues(float health, float damage, int scorePoints) {
        this.baseHealth = health;
        this.baseDamage = damage;
        this.scorePoints = scorePoints;
    }

    /**
     * Moves the enemy.
     */
    protected void move() {
        this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
        this.positionChange.setLength(waitTime / 1000f * this.speed);
        this.position.setX(this.position.getX() + this.positionChange.getX());
        this.position.setY(this.position.getY() + this.positionChange.getY());
    }
}
