package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Defines the shooter enemy type. The enemy chases and shoots the player.
 */
public class Shooter extends Enemy {
    /** The delay in ticks between shots. */
    private static final int shotTickDelay = 60;
    /** The time it takes the enemy to forget the player if out of detection range. */
    private static final int ticksToForget = 100;
    /** The distance from which the enemy detects the player. */
    private static final float detectDistance = 3f;
    /** The closest approach distance to the player. */
    private static final float stopDistance = 1f;

    /** True if the enemy is shooting, otherwise false. */
    private boolean shooting;
    /** True if the enemy has detected and is chasing the player, otherwise false. */
    private boolean detectedPlayer;
    /** The current tick in the attack cycle. */
    private int shootingTick;
    /** The amount of ticks remaining until the enemy will forget about the player, if the player is out of range. */
    private int forgetTick;

    /**
     * Constructs a Shooter enemy.
     * <p>
     * @param position  The position of the enemy.
     * @param manager   The manager associated with the enemy.
     * @param view      The main game view.
     */
    public Shooter(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.speed = 0.3f;
        this.size = 0.3f;
        this.paint.setColor(Color.GREEN);

        this.shooting = false;
        this.detectedPlayer = false;
        this.shootingTick = 0;
        this.forgetTick = 0;

        this.assignBasicValues(60f, 5f, 2);

        this.damage = baseDamage * (0.5f + 0.5f * this.manager.getLevel());
        this.health = baseHealth * (0.9f + 0.1f * this.manager.getLevel() * this.manager.getLevel());
        this.maxHealth = this.health;
    }

    /**
     * Defines the behavior of the Shooter.
     */
    @Override
    protected void behave() {
        super.behave();

        float distToPlayer = Vector2.distance(position, manager.getPlayer().getPosition());
        boolean moved = false;
        if (distToPlayer <= detectDistance) {
            // Detect the player if close enough
            this.detectedPlayer = true;
            this.forgetTick = ticksToForget;
            if (!shooting) {
                this.shooting = true;
                this.shootingTick = 0;
            }
            if (distToPlayer >= stopDistance) {
                move();
                moved = true;
            }
        }
        else if (distToPlayer > detectDistance) {
            // Start forgetting about the player if too far
            if (this.forgetTick > 0) {
                this.forgetTick--;
            }
            else if (this.forgetTick == 0) {
                this.detectedPlayer = false;
                this.shooting = false;
                this.shootingTick = 0;
            }
        }
        if (this.detectedPlayer) {
            // Move and charge shots while the player is detected
            if (!moved && distToPlayer >= stopDistance) {
                move();
            }
            this.shootingTick = (this.shootingTick + 1) % shotTickDelay;
            if (shootingTick == 0) {
                shoot();
            }
        }
    }

    /**
     * Shoots a projectile.
     */
    private void shoot() {
        this.manager.addEnemyProjectile(new Projectile(this.damage, this.position,
                Vector2.sub(position, manager.getPlayer().getPosition()), this.view));
    }
}
