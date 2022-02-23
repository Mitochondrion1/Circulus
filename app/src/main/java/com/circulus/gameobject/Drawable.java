package com.circulus.gameobject;

import android.graphics.Canvas;

/**
 * An interface for drawable objects.
 */
public interface Drawable {
    /**
     * Draw the object.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    void draw(Canvas canvas);
}
