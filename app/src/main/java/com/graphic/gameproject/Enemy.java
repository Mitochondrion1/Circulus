package com.graphic.gameproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Enemy implements Runnable {
    protected float health;
    protected float damage;
    protected Vector2 position;
    protected Vector2 pixelPosition;
    protected MainView view;
    protected Paint paint;
    protected boolean alive;
    protected Manager manager;
    protected Thread thread;

    public Enemy(Vector2 position, Manager manager, MainView view) {
        alive = true;
        this.position = position;
        this.manager = manager;
        this.view = view;
        //findPixelPosition();
        pixelPosition = view.positionToPixels(view.relativePosition(position));

        paint = new Paint();
        paint.setColor(Color.CYAN);

        thread = new Thread();
        thread.start();
    }

    @Override
    public void run() {
        while (alive) {
            findPixelPosition();
            behave();

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        alive = false;
    }

    protected void behave() {

    }

    public boolean isAlive() {
        return alive;
    }

    private void findPixelPosition() {
        pixelPosition = view.positionToPixels(view.relativePosition(position));
    }

    public void draw(Canvas canvas) {
        float width = 100f, height = 100f;
        canvas.drawRect(pixelPosition.getX() - width / 2, pixelPosition.getY() - height / 2,
                pixelPosition.getX() + width / 2, pixelPosition.getY() + height / 2, paint);
    }
}
