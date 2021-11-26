package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

// The game view
public class MainView extends View {
    private Player player;
    private Manager manager;
    private MainActivity activity;

    private Vector2 displaySize;
    private float widthInUnits;
    private float pixelsPerUnit;

    private Paint paint;

    private boolean showDirections;
    private float dirLength;
    private Paint dirPaint1, dirPaint2;

    public MainView(MainActivity activity) {
        super(activity);

        displaySize = DisplayParams.getDisplaySize(activity);
        widthInUnits = 5f;
        pixelsPerUnit = displaySize.getX() / widthInUnits;

        if (MainActivity.getNewGame()) {
            manager = new Manager(this, 1, 0);
        }
        else {
            manager = new Manager(this, Store.readInt(activity.getApplicationContext(), R.string.level_key, 1),
                    Store.readInt(activity.getApplicationContext(), R.string.kills_key, 0));
        }
        player = manager.getPlayer();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50f);

        showDirections = Store.readBool(activity.getApplicationContext(), R.string.direction_display_key, false);
        dirLength = 0.75f * getPixelsPerUnit();
        if (showDirections) {
            dirPaint1 = new Paint();
            dirPaint1.setStrokeWidth(3f);
            dirPaint1.setColor(Color.RED);

            dirPaint2 = new Paint();
            dirPaint2.setStrokeWidth(3f);
            dirPaint2.setColor(Color.BLUE);
        }
        this.activity = activity;
    }

    // Draw on screen
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background
        manager.drawBackground(canvas);

        // Draw shot and movement directions
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

        // Draw projectiles, enemies, the player, health packs and the map bounds
        manager.drawProjectiles(canvas);
        player.draw(canvas);
        manager.drawEnemies(canvas);
        manager.drawHealthPacks(canvas);
        manager.drawBounds(canvas);

        // Draw text with different values on the top-lef corner of the screen
        //canvas.drawText(player.getPosition().toString(), 5f, 55f, paint);
        //canvas.drawText("Health Packs: " + manager.getHealthPacks().size(), 5f, 110f, paint);
        canvas.drawText("Level: " + manager.getLevel(), 5f ,55f, paint);
        //canvas.drawText("Enemies left: " + manager.getEnemiesLeft(), 5f, 220f, paint);

        invalidate();
    }

    public Player getPlayer() {
        return manager.getPlayer();
    }

    // Find a vector that represents a vector relative to the player's position
    public Vector2 relativePosition(Vector2 position) {
        return new Vector2(position.getX() - player.getPosition().getX(),
                position.getY() - player.getPosition().getY());
    }

    // Convert world space relative position vector to pixel position vector
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

    public MainActivity getActivity() {
        return activity;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
