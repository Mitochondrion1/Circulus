package com.graphic.gameproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MainView extends View {
    private Player player;
    private Enemy enemy;

    private Vector2 displaySize;
    private float widthInUnits;
    private float pixelsPerUnit;

    private Paint paint;

    public MainView(Context context) {
        super(context);

        player = new Player(100, new Vector2(0, 0), this);

        displaySize = DisplayParams.getDisplaySize(context);
        widthInUnits = 5f;
        pixelsPerUnit = displaySize.getX() / widthInUnits;

        enemy = new Enemy(new Vector2(5f, 1f), null, this);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        player.draw(canvas);
        enemy.draw(canvas);
        canvas.drawText(player.getPosition().toString(), 5f, 55f, paint);
        canvas.drawText(player.getVelocity().toString(), 5f, 110f, paint);
        canvas.drawText(enemy.getPixelPosition().toString(), 5f, 165f, paint);

        invalidate();
    }

    public Player getPlayer() {
        return player;
    }

    public Vector2 relativePosition(Vector2 position) {
        return new Vector2(position.getX() - player.getPosition().getX(),
                position.getY() - player.getPosition().getY());
    }

    public Vector2 positionToPixels(Vector2 position) {
        return new Vector2(position.getX() * pixelsPerUnit + displaySize.getX() / 2,
                position.getY() * pixelsPerUnit + displaySize.getY() / 2);
    }
}
