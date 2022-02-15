package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Defines the Laserman enemy type. Shoots a beam that damages the player over time.
 */
public class Laserman extends Enemy {
    /** The amount of ticks the attack is on cool down. */
    private static final int cooldownTicks = 150;
    /** The amount of ticks it takes to charge the attack. */
    private static final int chargeTicks = 150;
    /** The amount of ticks that the attack charges and doesn't change direction. */
    private static final int finalChargeTicks = 75;
    /** The length of the attack in ticks. */
    private static final int shotTickLength = 50;
    /** The distance from which an enemy detects the player. */
    private static final float detectDistance = 2f;
    /** The closest an enemy approaches to the player. */
    private static final float closestMoveDistance = 1.2f;
    /** The maximum distance from the player which can trigger a shot. */
    private static final float maxShotDistance = 3f;

    /** The direction of the shot. */
    private Vector2 shotDirection;
    /** True if the enemy is charging the beam, otherwise false. */
    private boolean isCharging;
    /** True if the enemy is in the final stage of charging the beam, otherwise false. */
    private boolean isFinalCharging;
    /** True if the enemy is shooting the beam, otherwise false. */
    private boolean isShooting;
    /** True if the attack is on cool down, otherwise false. */
    private boolean isOnCoolDown;
    /** True if the enemy has detected the player, otherwise false. */
    private boolean detectedPlayer;
    /** The current tick in the attack cycle. */
    private int tick;

    /** The paint of the beam if the enemy is charging the attack. */
    private Paint laserPaint1;
    /** The paint of the beam when the enemy shoots. */
    private Paint laserPaint2;

    /**
     * Constructs a Laserman enemy.
     * <p>
     * @param position  The position of the enemy.
     * @param manager   The manager associated with the enemy.
     * @param view      The main game view.
     */
    public Laserman(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.speed = 0.2f;
        this.size = 0.4f;
        this.shotDirection = new Vector2(0f, 0f);
        this.detectedPlayer = false;
        this.isCharging = false;
        this.isFinalCharging = false;
        this.isShooting = false;
        this.isOnCoolDown = false;
        this.tick = 0;
        this.paint.setColor(0xffa0a0a0);

        this.laserPaint1 = new Paint();
        this.laserPaint1.setColor(Color.RED);
        this.laserPaint1.setAlpha(100);
        this.laserPaint1.setStrokeWidth(10f);

        this.laserPaint2 = new Paint();
        this.laserPaint2.setColor(Color.RED);
        this.laserPaint2.setStrokeWidth(10f);

        this.assignBasicValues(80f, 1f, 5);

        this.health = baseHealth * (0.9f + 0.1f * this.manager.getLevel() * this.manager.getLevel());
        this.maxHealth = this.health;
        this.damage = baseDamage * (0.8f + 0.2f * this.manager.getLevel());
    }

    /**
     * Draw the enemy and its beam.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    @Override
    public void draw(Canvas canvas) {
        // Draw the shot of an enemy while the shot is charging
        if (isCharging || isFinalCharging) {
            canvas.drawLine(this.pixelPosition.getX(), this.pixelPosition.getY(),
                    this.pixelPosition.getX() + this.shotDirection.getX() * view.getPixelsPerUnit(),
                    this.pixelPosition.getY() + this.shotDirection.getY() * view.getPixelsPerUnit(), laserPaint1);
        }

        // Draw the shot of an enemy while shooting
        else if (isShooting) {
            canvas.drawLine(this.pixelPosition.getX(), this.pixelPosition.getY(),
                    this.pixelPosition.getX() + this.shotDirection.getX() * view.getPixelsPerUnit(),
                    this.pixelPosition.getY() + this.shotDirection.getY() * view.getPixelsPerUnit(), laserPaint2);
        }

        // Draw the enemy itself
        super.draw(canvas);
    }

    /**
     * Defines the behavior of the Laserman.
     */
    @Override
    protected void behave() {
        super.behave();

        float distToPlayer = Vector2.distance(this.position, this.manager.getPlayer().getPosition());
        if (!detectedPlayer && (distToPlayer <= detectDistance || this.health < this.maxHealth)) {
            // Trigger the enemy if close enough to the player or hit
            detectedPlayer = true;
            isOnCoolDown = true;
        }
        if (isOnCoolDown) {
            // What happens when the enemy is on cool down
            this.tick++;
            if (distToPlayer > maxShotDistance) {
                this.tick = 0;
            }
            if (distToPlayer > closestMoveDistance) {
                move();
            }
            if (this.tick == cooldownTicks && distToPlayer <= maxShotDistance) {
                isOnCoolDown = false;
                isCharging = true;
                this.tick = 0;
            }
        }
        else if (isCharging) {
            // What happens when the enemy is charging the attack
            this.tick++;
            this.shotDirection = Vector2.sub(this.position, this.manager.getPlayer().getPosition());
            if (this.shotDirection.getLength() != 0) {
                this.shotDirection.setLength(10f);
            }
            if (this.tick == chargeTicks) {
                isCharging = false;
                isFinalCharging = true;
                this.tick = 0;
            }
        }
        else if (isFinalCharging) {
            // What happens when the enemy final-charges the attack
            this.tick++;
            if (this.tick == finalChargeTicks) {
                isFinalCharging = false;
                isShooting = true;
                this.tick = 0;
            }
        }
        else if (isShooting) {
            // What happens when the enemy is shooting
            this.tick++;
            Vector2 v = Vector2.sub(this.position, this.manager.getPlayer().getPosition());
            float cos = (v.getX() * shotDirection.getX() + v.getY() * shotDirection.getY()) / (v.getLength() * shotDirection.getLength());
            if (cos > (float)Math.sqrt(1 - Math.pow((this.manager.getPlayer().getSize() / 2) / v.getLength(), 2))) {
                // Damage the player if they get hit by the laser
                this.manager.getPlayer().damage(this.damage);
            }
            if (this.tick == shotTickLength) {
                isShooting = false;
                isOnCoolDown = true;
                this.tick = 0;
            }
        }
    }
}
