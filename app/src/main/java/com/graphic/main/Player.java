package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player extends Entity implements Runnable {
    private MainView view;
    private Manager manager;
    private Paint paint;

    private float displayWidth;
    private float displayHeight;
    private long waitTime;
    private int projectileTickDelay;
    private int tick;

    private float startX, startY, endX, endY;
    private float shotStartX, shotStartY, shotEndX, shotEndY;

    public Player(Vector2 position, float speed, float maxHealth, float damage, MainView view, Manager manager) {
        super(position, speed, maxHealth, damage);

        this.view = view;
        this.manager = manager;
        paint = new Paint();
        paint.setColor(Color.WHITE);

        displayWidth = DisplayParams.getDisplaySize(view).getX();
        displayHeight = DisplayParams.getDisplaySize(view).getY();
        waitTime = 15;
        projectileTickDelay = 10;
        tick = 0;

        startX = 0;
        startY = 0;
        endX = 0;
        endY = 0;
    }

    @Override
    public void run() {
        while (health > 0) {
            behave();
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(displayWidth / 2, displayHeight / 2, 50f, paint);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public void setShotStartX(float shotStartX) {
        this.shotStartX = shotStartX;
    }

    public void setShotStartY(float shotStartY) {
        this.shotStartY = shotStartY;
    }

    public void setShotEndX(float shotEndX) {
        this.shotEndX = shotEndX;
    }

    public void setShotEndY(float shotEndY) {
        this.shotEndY = shotEndY;
    }

    @Override
    protected void behave() {
        velocity.setX(0.02f * (endX - startX));
        velocity.setY(0.02f * (endY - startY));
        if (velocity.getLength() != 0) {
            velocity.setLength(1f);
        }
        if (shotEndX - shotStartX != 0 || shotEndY - shotStartY != 0) {
            if (tick == 0) {
                summonProjectile();
            }
        }
        position.setX(position.getX() + (waitTime / 1000f) * velocity.getX());
        position.setY(position.getY() + (waitTime / 1000f) * velocity.getY());
        tick = (tick + 1) % 10;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    private void summonProjectile() {
        Vector2 projVelocity = new Vector2(shotEndX - shotStartX, shotEndY - shotStartY);
        this.manager.addPlayerProjectile(new Projectile(this.damage, this.position, projVelocity, true, this.view));
    }
}
