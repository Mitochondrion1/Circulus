package com.game.main;

import android.graphics.Canvas;
import android.graphics.Paint;

// Defines the health pack
public class HealthPack implements Drawable {
    private float size;
    private float healAmount;
    private Vector2 position;
    private Paint paint, crossPaint;
    private MainView view;

    public HealthPack(Vector2 position, MainView view) {
        this.size = 0.1f;
        this.healAmount = 20f;
        this.position = position;
        this.view = view;

        this.paint = new Paint();
        this.paint.setColor(0xffffffff);

        this.crossPaint = new Paint();
        this.crossPaint.setColor(0xff00ff00);
    }

    public float getSize() {
        return size;
    }

    public float getHealAmount() {
        return healAmount;
    }

    public Vector2 getPosition() {
        return position;
    }

    // Draw the health pack
    @Override
    public void draw(Canvas canvas) {
        Vector2 pixelPosition = this.view.positionToPixels(this.view.relativePosition(this.position));
        float pixelSize = this.size * this.view.getPixelsPerUnit();
        canvas.drawCircle(pixelPosition.getX(), pixelPosition.getY(), pixelSize / 2, paint);
        canvas.drawRect(pixelPosition.getX() - pixelSize / 6, pixelPosition.getY() - pixelSize / 3,
                pixelPosition.getX() + pixelSize / 6, pixelPosition.getY() + pixelSize / 3, crossPaint);
        canvas.drawRect(pixelPosition.getX() - pixelSize / 3, pixelPosition.getY() - pixelSize / 6,
                pixelPosition.getX() + pixelSize / 3, pixelPosition.getY() + pixelSize / 6, crossPaint);
    }
}
