package com.graphic.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MainView extends View {
    private Player player;
    private Enemy enemy1, enemy2, enemy3, enemy4, enemy5;
    private Manager manager;

    private Vector2 displaySize;
    private float widthInUnits;
    private float pixelsPerUnit;

    private Paint paint;

    private boolean showDirections;
    private float dirLength;
    private Paint dirPaint1, dirPaint2;

    public MainView(Context context) {
        super(context);

        displaySize = DisplayParams.getDisplaySize(context);
        widthInUnits = 5f;
        pixelsPerUnit = displaySize.getX() / widthInUnits;

        manager = new Manager(this);
        player = manager.getPlayer();

        enemy1 = new Exploder(new Vector2(1f, 1f), 0.5f, 100f, 20f, manager, this);
        enemy2 = new Exploder(new Vector2(-1f, 1.5f), 0.5f, 100f, 20f, manager, this);
        enemy3 = new Exploder(new Vector2(-0.5f, 0.5f), 0.5f, 100f, 20f, manager, this);
        enemy4 = new Summoner(new Vector2(-3f, 5f), 0.5f, 100f, 20f, manager, this);
        enemy5 = new Laserman(new Vector2(3f, 5f), 0.2f, 150f, 1f, manager, this);

        manager.addEnemy(enemy1);
        manager.addEnemy(enemy2);
        manager.addEnemy(enemy3);
        manager.addEnemy(enemy4);
        manager.addEnemy(enemy5);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50f);

        showDirections = Store.readBool(context.getApplicationContext(), R.string.direction_display_key, false);
        dirLength = 0.75f * getPixelsPerUnit();
        if (showDirections) {
            dirPaint1 = new Paint();
            dirPaint1.setStrokeWidth(3f);
            dirPaint1.setColor(Color.RED);

            dirPaint2 = new Paint();
            dirPaint2.setStrokeWidth(3f);
            dirPaint2.setColor(Color.BLUE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        manager.drawBackground(canvas);
        if (showDirections) {
            canvas.drawLine(displaySize.getX() / 2, displaySize.getY() / 2,
                    displaySize.getX() / 2 + player.getVelocity().getX() * dirLength,
                    displaySize.getY() / 2 + player.getVelocity().getY() * dirLength,
                    dirPaint2);
            canvas.drawLine(displaySize.getX() / 2, displaySize.getY() / 2,
                    displaySize.getX() / 2 + player.getProjectileVelocityNormalized().getX() * dirLength,
                    displaySize.getY() / 2 + player.getProjectileVelocityNormalized().getY() * dirLength,
                    dirPaint1);
        }
        manager.drawProjectiles(canvas);
        player.draw(canvas);
        manager.drawEnemies(canvas);

        canvas.drawText(player.getPosition().toString(), 5f, 55f, paint);
        canvas.drawText(player.getVelocity().toString(), 5f, 110f, paint);

        invalidate();
    }

    public Player getPlayer() {
        return manager.getPlayer();
    }

    public Vector2 relativePosition(Vector2 position) {
        return new Vector2(position.getX() - player.getPosition().getX(),
                position.getY() - player.getPosition().getY());
    }

    public Vector2 positionToPixels(Vector2 position) {
        return new Vector2(position.getX() * pixelsPerUnit + displaySize.getX() / 2,
                position.getY() * pixelsPerUnit + displaySize.getY() / 2);
    }

    public float getWidthInUnits() {
        return this.widthInUnits;
    }

    public Vector2 getDisplaySize() {
        return this.displaySize;
    }

    public float getPixelsPerUnit() {
        return pixelsPerUnit;
    }
}
