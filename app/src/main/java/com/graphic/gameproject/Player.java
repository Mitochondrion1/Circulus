package com.graphic.gameproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player implements Runnable {
    private float health;
    private Vector2 position;
    private Vector2 velocity;
    private MainView view;
    private Paint paint;
    private Thread thread;

    private float displayWidth;
    private float displayHeight;
    private long waitTime;

    private float startX, startY, endX, endY;

    public Player(float health, Vector2 position, MainView view) {
        this.health = health;
        this.position = position;
        this.view = view;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        velocity = new Vector2(0, 0);

        displayWidth = DisplayParams.getDisplaySize(view).getX();
        displayHeight = DisplayParams.getDisplaySize(view).getY();
        waitTime = 15;

        startX = 0;
        startY = 0;
        endX = 0;
        endY = 0;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (health > 0) {
            velocity.setX(0.02f * (endX - startX));
            velocity.setY(0.02f * (endY - startY));
            if (velocity.getLength() != 0) {
                velocity.setLength(1f);
            }
            position.setX(position.getX() + (waitTime / 1000f) * velocity.getX());
            position.setY(position.getY() + (waitTime / 1000f) * velocity.getY());
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas) {
        float width = 100f, height = 100f;
        canvas.drawRect((displayWidth - width) / 2, (displayHeight - height) / 2,
                (displayWidth + width) / 2, (displayHeight + height) / 2, paint);
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
}
