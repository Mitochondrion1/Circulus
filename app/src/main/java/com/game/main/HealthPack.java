package com.game.main;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * A health pack that heals the player.
 */
public class HealthPack implements Drawable {
    /** The diameter of the health pack. */
    private float size;
    /** The amount of health the health pack heals. */
    private float healAmount;
    /** The position of the health pack. */
    private Vector2 position;
    /** The main paint of the pack. */
    private Paint paint;
    /** The paint of the cross on the pack. */
    private Paint crossPaint;
    /** The main game view. */
    private MainView view;

    /**
     * Constructs a health pack.
     * <p>
     * @param position  The position of the health pack.
     * @param view      The main game view.
     */
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

    /**
     * Get the size of the health pack.
     * <p>
     * @return The size (diameter) of the health pack.
     */
    public float getSize() {
        return size;
    }

    /**
     * Get the heal amount of the health pack.
     * <p>
     * @return The heal amount of the pack.
     */
    public float getHealAmount() {
        return healAmount;
    }

    /**
     * Get the position of the health pack.
     * @return The position of the pack.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Draws the health pack.
     * <p>
     * @param canvas The canvas used for drawing.
     */
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
