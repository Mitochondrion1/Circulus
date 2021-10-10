package com.graphic.gameproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Enemy implements Runnable {
    protected float health;
    protected float damage;
    protected Vector2 position;
    protected Vector2 pixelPosition;
    protected Vector2 positionChange;
    protected long waitTime;
    protected MainView view;
    protected Paint paint;
    protected boolean alive;
    protected Manager manager;
    protected Thread thread;

    protected int directorCost;
    protected float baseHealth;
    protected float baseDamage;

    public Enemy(Vector2 position, Manager manager, MainView view) {
        alive = true;
        this.position = position;
        this.manager = manager;
        this.view = view;
        //findPixelPosition();
        pixelPosition = this.view.positionToPixels(this.view.relativePosition(this.position));
        waitTime = 15;

        paint = new Paint();
        paint.setColor(Color.CYAN);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            this.pixelPosition = this.findPixelPosition();
            behave();
            //Log.d("Is running", "Yes");

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //alive = false;
    }

    protected void behave() {

    }

    public boolean isAlive() {
        return alive;
    }

    protected Vector2 findPixelPosition() {
        return view.positionToPixels(view.relativePosition(position));
        //Log.d("px pos", String.valueOf(pixelPosition));
    }

    public void draw(Canvas canvas) {
        float width = 100f, height = 100f;
        canvas.drawRect(pixelPosition.getX() - width / 2, pixelPosition.getY() - height / 2,
                pixelPosition.getX() + width / 2, pixelPosition.getY() + height / 2, paint);
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
}
