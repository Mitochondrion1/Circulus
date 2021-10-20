package com.graphic.main;

public class Exploder extends Enemy {
    private float velocity;

    private boolean exploding;
    private int explosionCountdown;

    public Exploder(Vector2 position, float speed, float maxHealth, float damage, Manager manager, MainView view) {
        super(position, speed, maxHealth, damage, manager, view);

        this.manager = manager;
        this.velocity = 0.5f;
        this.size = 0.3f;
        this.exploding = false;
        this.explosionCountdown = 0;

        this.directorCost = EnemyTypeData.EXPLODER.getDirectorCost();
        this.baseDamage = EnemyTypeData.EXPLODER.getBaseDamage();
        this.baseHealth = EnemyTypeData.EXPLODER.getBaseHealth();

        this.damage = baseDamage;
        this.health = baseHealth;
    }

    @Override
    protected void behave() {
        if (Vector2.distance(position, manager.getPlayer().getPosition()) != 0) {
            this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
            this.positionChange.setLength(waitTime / 1000f * velocity);
            this.position.setX(this.position.getX() + this.positionChange.getX());
            this.position.setY(this.position.getY() + this.positionChange.getY());
        }
        if (this.exploding) {
            this.explosionCountdown--;
            if (this.explosionCountdown == 0) {
                if (Vector2.distance(position, manager.getPlayer().getPosition()) <
                        0.75f * (this.size + manager.getPlayer().getSize())) {
                    manager.getPlayer().damage(this.damage);
                }
                this.alive = false;
            }
        }
        else if (Vector2.distance(position, manager.getPlayer().getPosition()) <
                    0.75f * (this.size + manager.getPlayer().getSize())) {
            this.exploding = true;
            this.explosionCountdown = 50;
        }
    }
}
