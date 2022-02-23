package com.circulus.gameobject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.circulus.playtime.MainView;
import com.circulus.playtime.Manager;
import com.circulus.utility.Vector2;

/**
 * Defines the Exploder enemy type. Explodes when close to the player. Damages the player are in the explosion AoE.
 */
public class Exploder extends Enemy {
    /** True if the explosion has been triggered already, otherwise false. */
    private boolean exploding;
    /** The amount of ticks left until the explosion. */
    private int explosionCountdown;
    /** The radius of the explosion AoE. */
    private float aoeRadius;

    /** The paint of the AoE. */
    private Paint aoePaint;

    /**
     * Constructs an Exploder enemy.
     * <p>
     * @param position  The position of the enemy.
     * @param manager   The manager associated with the enemy.
     * @param view      The main game view.
     */
    public Exploder(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.speed = 0.5f;
        this.size = 0.3f;
        this.exploding = false;
        this.explosionCountdown = 0;
        this.aoeRadius = 0.75f * (this.size + manager.getPlayer().getSize());
        this.paint.setColor(Color.CYAN);

        this.aoePaint = new Paint();
        this.aoePaint.setColor(Color.CYAN);
        this.aoePaint.setAlpha(0);

        this.assignBasicValues(40f, 20f, 2);

        this.damage = baseDamage * (0.8f + 0.2f * this.manager.getLevel());
        this.health = baseHealth * (0.9f + 0.1f * this.manager.getLevel() * this.manager.getLevel());
        this.maxHealth = this.health;
    }

    /**
     * Draws the enemy.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(this.findPixelPosition().getX(), this.findPixelPosition().getY(),
                aoeRadius * view.getPixelsPerUnit(), aoePaint);
        super.draw(canvas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Defines the behavior of the Exploder.
     */
    @Override
    protected void behave() {
        // Define the movement of the Exploder: homing the player from any distance
        if (Vector2.distance(position, manager.getPlayer().getPosition()) != 0) {
            move();
        }

        // Define what happens after the explosion begins
        if (this.exploding) {
            this.explosionCountdown--;
            if (explosionCountdown > 0) {
                this.paint.setARGB(255, (int)((1 - explosionCountdown / 50f) * 255), 255, 255);
            }
            else if (this.explosionCountdown == 0) {
                if (Vector2.distance(position, manager.getPlayer().getPosition()) <
                        0.75f * (this.size + manager.getPlayer().getSize()) + 0.5f * manager.getPlayer().getSize()) {
                    manager.getPlayer().damage(this.damage);
                }
                this.alive = false;
            }
        }

        // Start an explosion
        else if (Vector2.distance(position, manager.getPlayer().getPosition()) <
                    0.75f * (this.size + manager.getPlayer().getSize())) {
            this.exploding = true;
            this.explosionCountdown = 50;
            this.aoePaint.setAlpha(100);
        }
    }
}
