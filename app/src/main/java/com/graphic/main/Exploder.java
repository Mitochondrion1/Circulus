package com.graphic.main;

public class Exploder extends Enemy {
    private float damage;
    private float health;

    private float velocity;

    public Exploder(Vector2 position, float speed, float maxHealth, float damage, Manager manager, MainView view) {
        super(position, speed, maxHealth, damage, manager, view);

        this.manager = manager;
        this.velocity = 0.5f;

        this.directorCost = EnemyTypeData.EXPLODER.getDirectorCost();
        this.baseDamage = EnemyTypeData.EXPLODER.getBaseDamage();
        this.baseHealth = EnemyTypeData.EXPLODER.getBaseHealth();
    }

    @Override
    protected void behave() {
        if (Vector2.distance(position, manager.getPlayer().getPosition()) != 0) {
            this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
            this.positionChange.setLength(waitTime / 1000f * velocity);
            this.position.setX(this.position.getX() + this.positionChange.getX());
            this.position.setY(this.position.getY() + this.positionChange.getY());
        }
    }
}
