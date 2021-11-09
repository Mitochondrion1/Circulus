package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

// Defines the Laserman enemy type
public class Laserman extends Enemy {
    private static final int cooldownTicks = 150;           // The amount of ticks the attack is on cool down
    private static final int chargeTicks = 150;             // The amount of ticks it takes to charge the attack
    private static final int finalChargeTicks = 75;         // The amount of ticks that the attack charges and doesn't change direction
    private static final int shotTickLength = 50;           // The length of the attack in ticks
    private static final float detectDistance = 2f;         // The distance from which an enemy detects the player
    private static final float closestMoveDistance = 1.2f;  // The closest an enemy approaches to the player
    private static final float maxShotDistance = 3f;        // The maximum distance from the player which can trigger a shot

    private Vector2 shotDirection;
    private boolean isCharging;
    private boolean isFinalCharging;
    private boolean isShooting;
    private boolean isOnCooldown;
    private boolean detectedPlayer;
    private int tick;

    private Paint laserPaint1, laserPaint2;

    public Laserman(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.speed = 0.2f;
        this.size = 0.4f;
        this.shotDirection = new Vector2(0f, 0f);
        this.detectedPlayer = false;
        this.isCharging = false;
        this.isFinalCharging = false;
        this.isShooting = false;
        this.isOnCooldown = false;
        this.tick = 0;
        this.paint.setColor(0xffa0a0a0);

        this.laserPaint1 = new Paint();
        this.laserPaint1.setColor(Color.RED);
        this.laserPaint1.setAlpha(100);
        this.laserPaint1.setStrokeWidth(10f);

        this.laserPaint2 = new Paint();
        this.laserPaint2.setColor(Color.RED);
        this.laserPaint2.setStrokeWidth(10f);

        this.directorCost = EnemyTypeData.LASERMAN.getDirectorCost();
        this.baseHealth = EnemyTypeData.LASERMAN.getBaseHealth();
        this.baseDamage = EnemyTypeData.LASERMAN.getBaseDamage();

        this.health = baseHealth * (0.8f + 0.2f * this.manager.getLevel());
        this.maxHealth = this.health;
        this.damage = baseDamage * (0.8f + 0.2f * this.manager.getLevel());
    }

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

    @Override
    protected void behave() {
        super.behave();

        float distToPlayer = Vector2.distance(this.position, this.manager.getPlayer().getPosition());
        if (!detectedPlayer && (distToPlayer <= detectDistance || this.health < this.maxHealth)) {
            // Trigger the enemy if close enough to the player or hit
            detectedPlayer = true;
            isOnCooldown = true;
        }
        if (isOnCooldown) {
            // What happens when the enemy is on cool down
            this.tick++;
            if (distToPlayer > maxShotDistance) {
                this.tick = 0;
            }
            if (distToPlayer > closestMoveDistance) {
                move();
            }
            if (this.tick == cooldownTicks && distToPlayer <= maxShotDistance) {
                isOnCooldown = false;
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
                isOnCooldown = true;
                this.tick = 0;
            }
        }
    }

    private void move() {
        this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
        this.positionChange.setLength(waitTime / 1000f * this.speed);
        this.position.setX(this.position.getX() + this.positionChange.getX());
        this.position.setY(this.position.getY() + this.positionChange.getY());
    }
}
