package com.circulus.gameobject;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.circulus.playtime.MainView;
import com.circulus.utility.Vector2;

/**
 * Arrows are drawn around the player and point to enemies. Change in transparency depending on the distance between the player and the enemy.
 */
public class Arrow implements Drawable {
    /** The position of the arrow. */
    private Vector2 position;
    /** The view in which the arrow is drawn. */
    private MainView view;
    /** The paint of the arrow. */
    private Paint paint;

    /**
     * Constructs an arrow object.
     * <p>
     * @param view The view to associate with the arrow (the main game view).
     */
    public Arrow(MainView view) {
        this.position = new Vector2(0f, 0f);
        this.view = view;

        this.paint = new Paint();
        this.paint.setColor(0xffffffff);
    }

    /**
     * Get the position of the arrow.
     * <p>
     * @return The position of the arrow.
     * */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set a new value for the arrow's position.
     * <p>
     * @param position A new position for the arrow. */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Change the alpha (transparency) of the arrow.
     * <p>
     * @param x The new alpha of the arrow.
     * */
    public void setAlpha(int x) {
        this.paint.setAlpha(x);
    }

    @Override
     /**
      * {@inheritDoc}
      */
    public void draw(Canvas canvas) {
        canvas.drawCircle(this.position.getX(), this.position.getY(),
                view.getPixelsPerUnit() * 0.05f, this.paint);
    }
}
