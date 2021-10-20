package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public abstract class Enemy extends Entity implements Runnable {
    protected Vector2 pixelPosition;
    protected Vector2 positionChange;
    protected long waitTime;
    protected Paint paint;
    protected boolean alive;
    protected Manager manager;

    protected int directorCost;
    protected float baseHealth;
    protected float baseDamage;

    public Enemy(Vector2 position, float speed, float maxHealth, float damage, Manager manager, MainView view) {
        super(position, speed, maxHealth, damage);

        alive = true;
        this.manager = manager;
        this.view = view;
        //findPixelPosition();
        pixelPosition = this.view.positionToPixels(this.view.relativePosition(this.position));
        waitTime = 15;

        paint = new Paint();
        paint.setColor(Color.CYAN);
    }

    @Override
    public void run() {
        while (health > 0) {
            this.pixelPosition = this.findPixelPosition();
            behave();
            //Log.d("Is running", "Yes");

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    protected Vector2 findPixelPosition() {
        return view.positionToPixels(view.relativePosition(position));
        //Log.d("px pos", String.valueOf(pixelPosition));
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(pixelPosition.getX(), pixelPosition.getY(),
                this.view.getPixelsPerUnit() * this.size / 2, paint);
        drawHealthBar(canvas);
    }

    public Vector2 getPixelPosition() {
        return this.pixelPosition;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void changePosition(Vector2 change) {
        //this.position.setX(this.position.getX() + change.getX());
        //this.position.setY(this.position.getY() + change.getY());
        findPixelPosition();
    }

    public void damage(float damage) {
        this.health -= damage;
    }

    public boolean detectHit(Projectile projectile) {
        return Vector2.distance(this.position, projectile.getPosition()) < this.size / 2 + projectile.getSize() / 2;
    }
}
