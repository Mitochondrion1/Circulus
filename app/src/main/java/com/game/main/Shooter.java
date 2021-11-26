package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;

public class Shooter extends Enemy {
    private static final int shotTickDelay = 60;        // The delay in ticks between shots
    private static final int ticksToForget = 100;       // The time it takes the enemy to forget the player if out of detection range
    private static final float detectDistance = 3f;     // The distance from which the enemy detects the player
    private static final float stopDistance = 1f;       // The closest approach distance to the player

    private boolean shooting;
    private boolean detectedPlayer;
    private int shootingTick;
    private int forgetTick;

    public Shooter(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.speed = 0.3f;
        this.size = 0.3f;
        this.paint.setColor(Color.GREEN);

        this.shooting = false;
        this.detectedPlayer = false;
        this.shootingTick = 0;
        this.forgetTick = 0;

        this.assignBasicValues(5, 60f, 5f);

        this.damage = baseDamage * (0.5f + 0.5f * this.manager.getLevel());
        this.health = baseHealth * (0.9f + 0.1f * this.manager.getLevel() * this.manager.getLevel());
        this.maxHealth = this.health;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

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

    private void move() {
        this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
        this.positionChange.setLength(waitTime / 1000f * this.speed);
        this.position.setX(this.position.getX() + this.positionChange.getX());
        this.position.setY(this.position.getY() + this.positionChange.getY());
    }

    // Shoot a projectile
    private void shoot() {
        this.manager.addEnemyProjectile(new Projectile(this.damage, this.position,
                Vector2.sub(position, manager.getPlayer().getPosition()), this.view));
    }
}
