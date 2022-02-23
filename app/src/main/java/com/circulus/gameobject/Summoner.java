package com.circulus.gameobject;

import android.graphics.Color;

import com.circulus.playtime.MainView;
import com.circulus.playtime.Manager;
import com.circulus.utility.Vector2;

/**
 * Defines the Summoner enemy type. Summons Shooters occasionally.
 */
public class Summoner extends Enemy {
    /** The delay in ticks between summons. */
    private static final int summonTickDelay = 800;
    /** The distance from the Summoner that Shooters spawn. */
    private static final float summonDistance = 1f;

    /** True if the enemy detected the player, otherwise false. */
    private boolean detectedPlayer;
    /** The current tick in the attack cycle. */
    private int tick;

    /**
     * Constructs a Summoner enemy.
     * <p>
     * @param position  The position of the enemy.
     * @param manager   The manager associated with the enemy.
     * @param view      The main game view.
     */
    public Summoner(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.speed = 0.2f;
        this.size = 0.5f;
        this.detectedPlayer = false;
        this.tick = 600;
        this.paint.setColor(Color.MAGENTA);

        this.assignBasicValues(100f, 0f, 15);

        this.health = baseHealth * (0.9f + 0.1f * this.manager.getLevel() * this.manager.getLevel());
        this.maxHealth = this.health;
        this.damage = baseDamage * (0.8f + 0.2f * this.manager.getLevel());
    }

    /**
     * Defines the behavior of the Summoner.
     */
    @Override
    protected void behave() {
        super.behave();

        if (detectedPlayer) {
            // Move and occasionally summon a Shooter
            move();
            tick = (tick + 1) % summonTickDelay;
            if (tick == 0) {
                Vector2 summonPos = new Vector2(this.position.getX(), this.position.getY() - summonDistance);
                Shooter shooter = new Shooter(summonPos, this.manager, this.view);
                shooter.startThread();
                this.manager.addEnemy(shooter);
            }
        }
        else if (Vector2.distance(this.manager.getPlayer().getPosition(), this.position) <= 2 || health < maxHealth) {
            // Trigger if the player is close or the enemy is hit
            detectedPlayer = true;
        }
    }
}
