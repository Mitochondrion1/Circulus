package com.graphic.main;

public class Summoner extends Enemy {
    private static final int summonTickDelay = 800;
    private static final float summonDistance = 1f;

    private boolean detectedPlayer;
    private int tick;

    public Summoner(Vector2 position, float speed, float maxHealth, float damage, Manager manager, MainView view) {
        super(position, speed, maxHealth, damage, manager, view);

        this.speed = 0.2f;
        this.size = 0.5f;
        this.detectedPlayer = false;
        this.tick = 600;
        this.paint.setColor(0xffffa0a0);

        this.directorCost = EnemyTypeData.SUMMONER.getDirectorCost();
        this.baseHealth = EnemyTypeData.SUMMONER.getBaseHealth();
        this.baseDamage = EnemyTypeData.SUMMONER.getBaseDamage();

        this.health = baseHealth;
        this.maxHealth = baseHealth;
        this.damage = baseDamage;
    }

    @Override
    protected void behave() {
        super.behave();

        if (Vector2.distance(this.manager.getPlayer().getPosition(), this.position) <= 2) {
            detectedPlayer = true;
        }
        if (detectedPlayer) {
            move();
            tick = (tick + 1) % summonTickDelay;
            if (tick == 0) {
                Vector2 summonPos = new Vector2(this.position.getX(), this.position.getY() - summonDistance);
                this.manager.addEnemy(new Shooter(summonPos, 0.3f, 100f, 10f, this.manager, this.view));
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
