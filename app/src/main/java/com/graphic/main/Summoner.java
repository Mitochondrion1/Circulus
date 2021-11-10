package com.graphic.main;

import android.graphics.Color;

// Represents the Summoner enemy type
public class Summoner extends Enemy {
    private static final int summonTickDelay = 800;     // The delay in ticks between summons
    private static final float summonDistance = 1f;     // The distance from the Summoner that Shooters spawn

    private boolean detectedPlayer;
    private int tick;

    public Summoner(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.speed = 0.2f;
        this.size = 0.5f;
        this.detectedPlayer = false;
        this.tick = 600;
        this.paint.setColor(Color.MAGENTA);

        this.directorCost = EnemyTypeData.SUMMONER.getDirectorCost();
        this.baseHealth = EnemyTypeData.SUMMONER.getBaseHealth();
        this.baseDamage = EnemyTypeData.SUMMONER.getBaseDamage();

        this.health = baseHealth * (0.8f + 0.2f * this.manager.getLevel());
        this.maxHealth = this.health;
        this.damage = baseDamage * (0.8f + 0.2f * this.manager.getLevel());
    }

    @Override
    protected void behave() {
        super.behave();

        if (detectedPlayer) {
            // Move and occasionally summon a Shooter
            move();
            tick = (tick + 1) % summonTickDelay;
            if (tick == 0) {
                Vector2 summonPos = new Vector2(this.position.getX(), this.position.getY() - summonDistance);
                this.manager.addEnemy(new Shooter(summonPos, this.manager, this.view));
            }
        }
        else if (Vector2.distance(this.manager.getPlayer().getPosition(), this.position) <= 2 || health < maxHealth) {
            // Trigger if the player is close or the enemy is hit
            detectedPlayer = true;
        }
    }

    private void move() {
        this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
        this.positionChange.setLength(waitTime / 1000f * this.speed);
        this.position.setX(this.position.getX() + this.positionChange.getX());
        this.position.setY(this.position.getY() + this.positionChange.getY());
    }
}
