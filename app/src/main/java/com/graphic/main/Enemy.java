package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Color;

// Defines an AI controlled enemy
public abstract class Enemy extends Entity implements Runnable {
    protected Vector2 pixelPosition;
    protected Vector2 positionChange;
    protected boolean alive;
    protected Manager manager;
    protected Arrow arrow;

    protected int directorCost;

    public Enemy(Vector2 position, Manager manager, MainView view) {
        super(position);

        alive = true;
        this.manager = manager;
        this.view = view;
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

    public void draw(Canvas canvas) {
        canvas.drawCircle(pixelPosition.getX(), pixelPosition.getY(),
                this.view.getPixelsPerUnit() * this.size / 2, paint);
        this.arrow.draw(canvas);
        drawHealthBar(canvas);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public int getDirectorCost() {
        return directorCost;
    }

    public void startThread() {
        thread.start();
    }
}
