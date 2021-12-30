package com.game.main;

import android.graphics.Canvas;

// Defines an AI controlled enemy
public abstract class Enemy extends Entity implements Runnable {
    protected Vector2 positionChange;
    protected boolean alive;
    protected Manager manager;
    protected Arrow arrow;

    protected int directorCost;
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

    public int getDirectorCost() {
        return directorCost;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public void startThread() {
        thread.start();
    }

    protected void assignBasicValues(int cost, float health, float damage, int scorePoints) {
        this.directorCost = cost;
        this.baseHealth = health;
        this.baseDamage = damage;
        this.scorePoints = scorePoints;
    }
}
