package com.game.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * The main game view.
 */
public class MainView extends View {
    /** A reference to the player object. */
    private Player player;
    /** The manager object. */
    private Manager manager;
    /** The activity this view is nested in. */
    private MainActivity activity;

    /** The display resolution. */
    private Vector2 displaySize;
    /** The width of the screen in world-space units. */
    private float widthInUnits;
    /** The number of pixel per world-space unit.*/
    private float pixelsPerUnit;

    /** The text paint. */
    private Paint paint;

    /** True if the movement and shot directions of the player should be shown, otherwise false. Triggered in settings. */
    private boolean showDirections;
    /** The length the direction displaying lines. */
    private float dirLength;
    /** The paint of the shot direction line. */
    private Paint dirPaint1;
    /** The paint of the movement direction line. */
    private Paint dirPaint2;

    /**
     * Construct the view.
     * <p>
     * @param activity The activity this view is nested in.
     */
    public MainView(MainActivity activity) {
        super(activity);

        this.activity = activity;

        //displaySize = DisplayParams.getDisplaySize(activity);
        displaySize = activity.getRealDisplaySize();
        widthInUnits = 5f;
        pixelsPerUnit = displaySize.getX() / widthInUnits;

        if (MainActivity.getNewGame()) {
            manager = new Manager(this, 1, 0, 0);
        }
        else {
            manager = new Manager(this, Store.readInt(activity.getApplicationContext(), R.string.level_key, 1),
                    Store.readInt(activity.getApplicationContext(), R.string.kills_key, 0),
                    Store.readInt(activity.getApplicationContext(), R.string.score_key, 0));
        }
        player = manager.getPlayer();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(displaySize.getY() / 20);

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
    }

    /**
     * Draw on screen.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background
        manager.drawBackground(canvas);

        // Draw shot and movement directions
        if (showDirections) {
            canvas.drawLine(displaySize.getX() / 2, displaySize.getY() / 2,
                    displaySize.getX() / 2 + player.getTrueVelocity().getX() * dirLength,
                    displaySize.getY() / 2 + player.getTrueVelocity().getY() * dirLength,
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
        canvas.drawText("Level: " + manager.getLevel(), 0.01f * displaySize.getX() ,0.06f * displaySize.getY(), paint);
        canvas.drawText(manager.getTime(), 0.01f * displaySize.getX(), 0.12f * displaySize.getY(), paint);
        canvas.drawText("Score: " + manager.getScore(), 0.01f * displaySize.getX(), 0.98f * displaySize.getY(), paint);

        invalidate();
    }

    /**
     * Get the player object.
     * <p>
     * @return The player object.
     */
    public Player getPlayer() {
        return manager.getPlayer();
    }

    /**
     * Find the position of a vector relative to the player.
     * <p>
     * @param position  The vector.
     * @return          The vector's position relative to the player.
     */
    public Vector2 relativePosition(Vector2 position) {
        return new Vector2(position.getX() - player.getPosition().getX(),
                position.getY() - player.getPosition().getY());
    }

    /**
     * Converts world-space position into pixel position.
     * <p>
     * @param position  The world-space position.
     * @return          The pixel position of the given vector.
     */
    public Vector2 positionToPixels(Vector2 position) {
        return new Vector2(position.getX() * pixelsPerUnit + displaySize.getX() / 2,
                position.getY() * pixelsPerUnit + displaySize.getY() / 2);
    }

    /**
     * Get tge width of the display in world-space units.
     * <p>
     * @return The width of the display in world-space units.
     */
    public float getWidthInUnits() {
        return this.widthInUnits;
    }

    /**
     * Get the display size.
     * <p>
     * @return The display size (in pixels).
     */
    public Vector2 getDisplaySize() {
        return this.displaySize;
    }

    /**
     * Get the amount of pixels per world-space unit.
     * <p>
     * @return Amount of pixels per world-space unit.
     */
    public float getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    /**
     * Get the activity this view is nested in.
     * <p>
     * @return The activity this view is nested in.
     */
    public MainActivity getActivity() {
        return activity;
    }

    /**
     * Get the manager object.
     * <p>
     * @return The manager object.
     */
    public Manager getManager() {
        return manager;
    }

    /**
     * Set the manger object.
     * @param manager The new manager object.
     */
    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
