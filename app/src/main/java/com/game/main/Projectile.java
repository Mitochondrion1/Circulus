package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

// Represents a projectile
public class Projectile implements Runnable {
    private float damage;
    private Vector2 position;
    private Vector2 velocity;
    private float speed;
    private float size;
    private int waitTime;
    private int lifetime;
    private boolean hitTarget;
    private Paint paint;
    private MainView view;
    private Thread thread;

    public Projectile(float damage, Vector2 position, Vector2 velocity, MainView view) {
        this.damage = damage;
        this.position = new Vector2(position.getX(), position.getY());
        this.velocity = velocity;
        this.size = 0.1f;
        speed = 2f;
        waitTime = 15;
        lifetime = 200;
        this.velocity.setLength(speed);
        hitTarget = false;
        this.view = view;

        this.paint = new Paint();
        paint.setColor(Color.YELLOW);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!hitTarget && lifetime > 0) {
            this.position.setX(this.position.getX() + (waitTime / 1000f) * this.velocity.getX());
            this.position.setY(this.position.getY() + (waitTime / 1000f) * this.velocity.getY());
            lifetime--;

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (lifetime == 0) {
            hitTarget = true;
        }
    }

    public float getDamage() {
        return damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isHitTarget() {
        return hitTarget;
    }

    public float getSize() {
        return size;
    }

    public void setHitTarget(boolean hasHitTarget) {
        this.hitTarget = hasHitTarget;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(findPixelPosition().getX(), findPixelPosition().getY(),
                this.view.getPixelsPerUnit() * this.size / 2, paint);
    }

    private Vector2 findPixelPosition() {
        return view.positionToPixels(view.relativePosition(this.position));
    }
}
