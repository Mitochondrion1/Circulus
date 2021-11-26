package com.game.main;

import android.graphics.Canvas;
import android.graphics.Paint;

// Defines the arrows around the player which point toward enemies
public class Arrow {
    private Vector2 position;
    private MainView view;
    private Paint paint;

    public Arrow(MainView view) {
        this.position = new Vector2(0f, 0f);
        this.view = view;

        this.paint = new Paint();
        this.paint.setColor(0xffffffff);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setAlpha(int x) {
        this.paint.setAlpha(x);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(this.position.getX(), this.position.getY(),
                view.getPixelsPerUnit() * 0.05f, this.paint);
    }
}
