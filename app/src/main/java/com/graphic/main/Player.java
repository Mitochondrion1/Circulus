package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Color;

// The class that represents the player
public class Player extends Entity implements Runnable {
    private Manager manager;

    private float displayWidth;
    private float displayHeight;
    private final int baseProjectileDelay = 20;
    private int projectileDelay;
    private int tick;

    // Multipliers for different values, used for upgrades
    private float healthMultiplier;
    private float damageMultiplier;
    private float attackSpeedMultiplier;

    // Touch/accelerometer values for calculating movement velocity and projectile shot direction
    private float startX, startY, endX, endY;
    private float shotStartX, shotStartY, shotEndX, shotEndY;

    public Player(Vector2 position, MainView view, Manager manager) {
        super(position);

        this.baseHealth = 100f;
        this.baseDamage = 10f;

        thread.start();

        this.damageMultiplier = 1f;
        this.healthMultiplier = 1f;
        this.attackSpeedMultiplier = 1f;

        this.speed = 1f;
        this.damage = this.damageMultiplier * baseDamage;
        this.maxHealth = this.healthMultiplier * baseHealth;
        this.health = this.maxHealth;
        this.projectileDelay = (int)(this.attackSpeedMultiplier * this.baseProjectileDelay);

        this.view = view;
        this.manager = manager;
        this.size = 0.3f;
        this.paint.setColor(Color.WHITE);

        displayWidth = DisplayParams.getDisplaySize(view).getX();
        displayHeight = DisplayParams.getDisplaySize(view).getY();
        tick = 0;

        startX = 0;
        startY = 0;
        endX = 0;
        endY = 0;
    }

    @Override
    public void run() {
        while (health > 0) {
            super.run();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(displayWidth / 2, displayHeight / 2,
                this.view.getPixelsPerUnit() * this.size / 2, paint);
        drawHealthBar(canvas);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public void setShotStartX(float shotStartX) {
        this.shotStartX = shotStartX;
    }

    public void setShotStartY(float shotStartY) {
        this.shotStartY = shotStartY;
    }

    public void setShotEndX(float shotEndX) {
        this.shotEndX = shotEndX;
    }

    public void setShotEndY(float shotEndY) {
        this.shotEndY = shotEndY;
    }

    public void heal(float hp) {
        this.health = Math.min(this.health + this.maxHealth * hp / 100f, this.maxHealth);
    }

    @Override
    protected void behave() {
        // Calculate the desired velocity of the player
        velocity.setX(endX - startX);
        velocity.setY(endY - startY);
        if (velocity.getLength() != 0) {
            velocity.setLength(1f);
        }

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

    // Set the current tick, required for shots
    public void setTick(int tick) {
        this.tick = tick;
    }

    // Normalize the projectile velocity
    public Vector2 getProjectileVelocityNormalized() {
        Vector2 vel = new Vector2(shotEndX - shotStartX, shotEndY - shotStartY);
        vel.setLength(1f);
        return vel;
    }

    // Shoot a projectile
    public void summonProjectile() {
        Vector2 projVelocity = new Vector2(shotEndX - shotStartX, shotEndY - shotStartY);
        this.manager.addPlayerProjectile(new Projectile(this.damage, this.position, projVelocity, true, this.view));
    }

    // Increase the maximum health of the player
    public void increaseHealth() {
        float healthPercentage = this.health / this.maxHealth;

        this.healthMultiplier += 0.4f;
        this.maxHealth = this.healthMultiplier * this.baseHealth;
        this.health = healthPercentage * this.maxHealth;
    }

    // Increase the damage of the player
    public void increaseDamage() {
        this.damageMultiplier += 0.4f;
        this.damage = this.damageMultiplier * this.baseDamage;
    }

    // Increase the attack sped of the player
    public void increaseAttackSpeed() {
        this.attackSpeedMultiplier *= 0.9f;
        this.projectileDelay = (int)Math.ceil(this.attackSpeedMultiplier * this.baseProjectileDelay);
    }

    // Is the player out of the bounds of the map
    private boolean isOutOfBounds() {
        return this.position.getX() - this.size / 2 < this.manager.getBoundLeft() ||
                this.position.getX() + this.size / 2 > this.manager.getBoundRight() ||
                this.position.getY() - this.size / 2 < this.manager.getBoundTop() ||
                this.position.getY() + this.size / 2 > this.manager.getBoundBottom();
    }
}
