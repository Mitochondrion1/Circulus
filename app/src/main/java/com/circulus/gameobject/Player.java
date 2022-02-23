package com.circulus.gameobject;

import android.graphics.Color;

import com.circulus.playtime.MainView;
import com.circulus.playtime.Manager;
import com.circulus.main.R;
import com.circulus.utility.Store;
import com.circulus.utility.Vector2;

/**
 * The player class.
 */
public class Player extends Entity implements Runnable {
    /** The base tick delay between shots. */
    private final int baseProjectileDelay = 20;
    /** The real tick delay between shots. */
    private int projectileDelay;
    /** The current tick in the attack cycle. */
    private int tick;

    /** The health multiplier. */
    private float healthMultiplier;
    /** The damage multiplier. */
    private float damageMultiplier;
    /** The attack speed multiplier. */
    private float attackSpeedMultiplier;

    /** The start x of the movement touch gesture. */
    private float startX;
    /** The start y of the movement touch gesture. */
    private float startY;
    /** The end x of the movement touch gesture. */
    private float endX;
    /** The end y of the movement touch gesture. */
    private float endY;
    /** The start x of the shot touch gesture. */
    private float shotStartX;
    /** The start y of the shot touch gesture. */
    private float shotStartY;
    /** The end x of the shot touch gesture. */
    private float shotEndX;
    /** The end y of the shot touch gesture. */
    private float shotEndY;

    /** The real velocity of the player at a certain time, unaffected during velocity calculations in values. */
    private Vector2 trueVelocity;

    /**
     * Constructs a player object.
     * <p>
     * @param position  The initial position of the player.
     * @param view      The main game view.
     * @param manager   The manager object associated with the player.
     */
    public Player(Vector2 position, MainView view, Manager manager) {
        super(position, view);

        this.pixelPosition = new Vector2(this.view.getDisplaySize().getX() / 2,
                this.view.getDisplaySize().getY() / 2);
        trueVelocity = new Vector2(0f, 0f);

        this.baseHealth = 100f;
        this.baseDamage = 10f;

        this.damageMultiplier = Store.readFloat(this.view.getActivity().getApplicationContext(), R.string.damage_key, 1f);
        this.healthMultiplier = Store.readFloat(this.view.getActivity().getApplicationContext(), R.string.health_key, 1f);
        this.attackSpeedMultiplier = Store.readFloat(this.view.getActivity().getApplicationContext(), R.string.attack_speed_key, 1f);

        this.speed = 1f;
        this.damage = this.damageMultiplier * baseDamage;
        this.maxHealth = this.healthMultiplier * baseHealth;
        this.health = this.maxHealth;
        this.projectileDelay = (int)(this.attackSpeedMultiplier * this.baseProjectileDelay);

        this.manager = manager;
        this.size = 0.3f;
        this.paint.setColor(Color.WHITE);

        tick = 0;

        startX = 0;
        startY = 0;
        endX = 0;
        endY = 0;

        thread.start();
    }

    @Override
    public void run() {
        while (health > 0) {
            super.run();
        }
    }

    /**
     * Get the position of the player.
     * <p>
     * @return Player's position.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set the start x of the movement touch gesture.
     * <p>
     * @param startX The new value.
     */
    public void setStartX(float startX) {
        this.startX = startX;
    }

    /**
     * Set the start y of the movement touch gesture.
     * <p>
     * @param startY The new value.
     */
    public void setStartY(float startY) {
        this.startY = startY;
    }

    /**
     * Set the end x of the movement touch gesture.
     * <p>
     * @param endX The new value.
     */
    public void setEndX(float endX) {
        this.endX = endX;
    }

    /**
     * Set the end y of the movement touch gesture.
     * <p>
     * @param endY The new value.
     */
    public void setEndY(float endY) {
        this.endY = endY;
    }

    /**
     * Set the start x of the shot touch gesture.
     * <p>
     * @param shotStartX The new value.
     */
    public void setShotStartX(float shotStartX) {
        this.shotStartX = shotStartX;
    }

    /**
     * Set the start y of the shot touch gesture.
     * <p>
     * @param shotStartY The new value.
     */
    public void setShotStartY(float shotStartY) {
        this.shotStartY = shotStartY;
    }

    /**
     * Set the end x of the shot touch gesture.
     * <p>
     * @param shotEndX The new value.
     */
    public void setShotEndX(float shotEndX) {
        this.shotEndX = shotEndX;
    }

    /**
     * Set the end y of the shot touch gesture.
     * <p>
     * @param shotEndY The new value.
     */
    public void setShotEndY(float shotEndY) {
        this.shotEndY = shotEndY;
    }

    /**
     * Heals the player.
     * <p>
     * @param hp The amount of health to heal.
     */
    public void heal(float hp) {
        this.health = Math.min(this.health + this.maxHealth * hp / 100f, this.maxHealth);
    }

    /**
     * Heals the player to full health.
     */
    public void healToFull() {
        this.health = this.maxHealth;
    }

    /**
     * Defines the Player's behaviour
     */
    @Override
    protected void behave() {
        // Calculate the desired velocity of the player
        velocity.setX(endX - startX);
        velocity.setY(endY - startY);
        if (velocity.getLength() != 0) {
            velocity.setLength(1f);
        }
        trueVelocity = new Vector2(velocity.getX(), velocity.getY());

        // Shoot a projectile under right conditions
        if (shotEndX - shotStartX != 0 || shotEndY - shotStartY != 0) {
            if (tick == 0) {
                summonProjectile();
            }
        }

        // Move the player based on the velocity calculated earlier
        position.setX(position.getX() + (waitTime / 1000f) * velocity.getX());
        position.setY(position.getY() + (waitTime / 1000f) * velocity.getY());

        // Update the current tick
        tick = (tick + 1) % projectileDelay;

        // Damage the player if they are out of the map bounds
        if (this.isOutOfBounds()) {
            this.damage(this.maxHealth / 300f);
        }
    }

    /**
     * Sets the current tick int the attack cycle.
     * <p>
     * @param tick The new value.
     */
    public void setTick(int tick) {
        this.tick = tick;
    }

    /**
     * Returns a normalized projectile velocity vector.
     * <p>
     * @return The normalized vector.
     */
    public Vector2 getProjectileVelocityNormalized() {
        Vector2 vel = new Vector2(shotEndX - shotStartX, shotEndY - shotStartY);
        vel.setLength(1f);
        return vel;
    }

    /**
     * Shoots a projectile.
     */
    public void summonProjectile() {
        Vector2 projVelocity = new Vector2(shotEndX - shotStartX, shotEndY - shotStartY);
        this.manager.addPlayerProjectile(new Projectile(this.damage, this.position, projVelocity, this.view));
    }

    /**
     * Increases the maximum health of the player.
     */
    public void increaseHealth() {
        float healthPercentage = this.health / this.maxHealth;

        this.healthMultiplier += 0.4f;
        this.maxHealth = this.healthMultiplier * this.baseHealth;
        this.health = healthPercentage * this.maxHealth;
    }

    /**
     * Increases the damage of the player.
     */
    public void increaseDamage() {
        this.damageMultiplier += 0.4f;
        this.damage = this.damageMultiplier * this.baseDamage;
    }

    /**
     * Increases the attack speed of the player.
     */
    public void increaseAttackSpeed() {
        this.attackSpeedMultiplier *= 0.9f;
        this.projectileDelay = (int)Math.ceil(this.attackSpeedMultiplier * this.baseProjectileDelay);
    }

    /**
     * Stores the multipliers for a loaded game.
     */
    public void saveMultipliers() {
        Store.saveFloat(view.getActivity().getApplicationContext(), R.string.damage_key, this.damageMultiplier);
        Store.saveFloat(view.getActivity().getApplicationContext(), R.string.health_key, this.healthMultiplier);
        Store.saveFloat(view.getActivity().getApplicationContext(), R.string.attack_speed_key, this.attackSpeedMultiplier);
    }

    /**
     * Checks if the player is out of the map bounds.
     * <p>
     * @return True if out of bounds, otherwise false.
     */
    private boolean isOutOfBounds() {
        return this.position.getX() - this.size / 2 < this.manager.getBoundLeft() ||
                this.position.getX() + this.size / 2 > this.manager.getBoundRight() ||
                this.position.getY() - this.size / 2 < this.manager.getBoundTop() ||
                this.position.getY() + this.size / 2 > this.manager.getBoundBottom();
    }

    /**
     * Get the true velocity vector.
     * <p>
     * @return The true velocity vector.
     */
    public Vector2 getTrueVelocity() {
        return trueVelocity;
    }
}
