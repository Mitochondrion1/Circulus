package com.game.main;

import android.graphics.Canvas;

// Defines an AI controlled enemy
public abstract class Enemy extends Entity implements Runnable {
    protected Vector2 positionChange;
    protected boolean alive;
    protected Arrow arrow;

    protected int scorePoints;

    public Enemy(Vector2 position, Manager manager, MainView view) {
        super(position, view);

        alive = true;
        this.manager = manager;
        this.arrow = new Arrow(this.view);
        this.arrow.setAlpha((int)Math.min(0xff, 0xff / Vector2.distance(
                this.manager.getPlayer().getPosition(), this.position)));
        pixelPosition = this.view.positionToPixels(this.view.relativePosition(this.position));
    }

    @Override
    public void run() {
        Vector2 arrowDir;
        while (health > 0) {
            this.pixelPosition = this.findPixelPosition();
            arrowDir = Vector2.sub(this.manager.getPlayer().getPosition(), this.position);
            arrowDir.setLength(0.5f);
            arrow.setPosition(view.positionToPixels(view.relativePosition(
                    new Vector2(this.manager.getPlayer().getPosition().getX() + arrowDir.getX(),
                    this.manager.getPlayer().getPosition().getY() + arrowDir.getY()))));
            this.arrow.setAlpha((int)Math.min(0xff, 4 * 0xff / Vector2.distance(
                    this.manager.getPlayer().getPosition(), this.position)));

            super.run();
        }
        alive = false;
    }

    protected Vector2 findPixelPosition() {
        return view.positionToPixels(view.relativePosition(position));
    }

    @Override
    public void draw(Canvas canvas) {
        this.arrow.draw(canvas);
        super.draw(canvas);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public void startThread() {
        thread.start();
    }

    protected void assignBasicValues(int cost, float health, float damage, int scorePoints) {
        this.baseHealth = health;
        this.baseDamage = damage;
        this.scorePoints = scorePoints;
    }

    protected void move() {
        this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
        this.positionChange.setLength(waitTime / 1000f * this.speed);
        this.position.setX(this.position.getX() + this.positionChange.getX());
        this.position.setY(this.position.getY() + this.positionChange.getY());
    }
}
