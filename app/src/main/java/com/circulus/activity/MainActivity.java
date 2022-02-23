package com.circulus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.circulus.dialog.*;
import com.circulus.playtime.MainView;
import com.circulus.playtime.Manager;
import com.circulus.playtime.MusicService;
import com.circulus.main.R;
import com.circulus.utility.Store;
import com.circulus.utility.Vector2;

/**
 * The main game activity, where the game itself runs.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    /** The main game view. */
    private MainView view;
    /** The intent to activate the music service. */
    private Intent musicServiceIntent;
    /** The resolution of the display. */
    private Vector2 displaySize;
    /** True if accelerometer mode is activated, otherwise false. */
    private boolean accelerometerMode;
    /** True if the level end dialog is displayed, otherwise false. */
    private boolean levelEndDialogShown;
    /** A string that represents the text displayed in the level end dialog. */
    private String increasedValueString;

    /** The accelerometer sensitivity, as it was selected in the settings. */
    private int accSensitivity;
    /** The time between accelerometer updates. */
    private static final int UPDATE_THRESHOLD = 15;
    /** The sensor manager. */
    private SensorManager mSensorManager;
    /** The accelerometer sensor. */
    private Sensor mAccelerometer;

    /** The time of the last accelerometer update. */
    private long mLastUpdate;

    /** True if the user selected to begin a new game, otherwise false. */
    private static boolean newGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        hideNavigation();

        musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);
        view = new MainView(this);
        displaySize = getRealDisplaySize();
        accelerometerMode = Store.readBool(getApplicationContext(), R.string.accelerometer_key, true);
        levelEndDialogShown = false;
        increasedValueString = "";
        accSensitivity = Store.readInt(getApplicationContext(), R.string.sensitivity_key, 2);

        // Register the accelerometer if in accelerometer mode
        if (accelerometerMode) {
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            if (null == (mAccelerometer = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)))
                finish();
        }

        // Start the music service
        startService(musicServiceIntent);

        // Set the content view
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometerMode) {
            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_UI);

            mLastUpdate = System.currentTimeMillis();
        }
    }

    /** True if a touch gesture was detected and the player should move, otherwise false. */
    private boolean move = false;
    /** True if a touch gesture was detected and the player should shoot, otherwise false. */
    private boolean shoot = false;
    /** The id of the move gesture. */
    private int moveId;
    /** The id of the shot gesture. */
    private int shotId;

    /**
     * Manages touch events.
     * <p>
     * @param event The touch event.
     * @return Returned value has no meaning.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDestroyed()) {
            if (accelerometerMode) {
                // In accelerometer mode use touch only for shots
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        view.getPlayer().setShotStartX(displaySize.getX() / 2);
                        view.getPlayer().setShotStartY(displaySize.getY() / 2);
                        view.getPlayer().setShotEndX(event.getX());
                        view.getPlayer().setShotEndY(event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        view.getPlayer().setShotStartX(0);
                        view.getPlayer().setShotStartY(0);
                        view.getPlayer().setShotEndX(0);
                        view.getPlayer().setShotEndY(0);
                        break;
                }
            }
            else {
                /*
                 * In touch mode use touch in the right side of the screen
                 * to move and on the left side to shoot
                 */
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (event.getX() >= displaySize.getX() / 2) {
                            view.getPlayer().setStartX(event.getX());
                            view.getPlayer().setStartY(event.getY());
                            view.getPlayer().setEndX(event.getX());
                            view.getPlayer().setEndY(event.getY());
                            move = true;
                            moveId = event.getPointerId(0);
                        }
                        else {
                            view.getPlayer().setShotStartX(event.getX());
                            view.getPlayer().setShotStartY(event.getY());
                            view.getPlayer().setShotEndX(event.getX());
                            view.getPlayer().setShotEndY(event.getY());
                            view.getPlayer().setTick(0);
                            shoot = true;
                            shotId = event.getPointerId(0);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (event.getX(event.getActionIndex()) >= displaySize.getX() / 2 && !move) {

                            view.getPlayer().setStartX(event.getX(event.getActionIndex()));
                            view.getPlayer().setStartY(event.getY(event.getActionIndex()));
                            view.getPlayer().setEndX(event.getX(event.getActionIndex()));
                            view.getPlayer().setEndY(event.getY(event.getActionIndex()));
                            move = true;
                            moveId = event.getPointerId(event.getActionIndex());
                        }
                        else if (event.getX(event.getActionIndex()) < displaySize.getX() / 2  && !shoot) {
                            view.getPlayer().setShotStartX(event.getX(event.getActionIndex()));
                            view.getPlayer().setShotStartY(event.getY(event.getActionIndex()));
                            view.getPlayer().setShotEndX(event.getX(event.getActionIndex()));
                            view.getPlayer().setShotEndY(event.getY(event.getActionIndex()));
                            view.getPlayer().setTick(0);
                            shoot = true;
                            shotId = event.getPointerId(event.getActionIndex());
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (move) {
                            try {
                                view.getPlayer().setEndX(event.getX(event.findPointerIndex(moveId)));
                                view.getPlayer().setEndY(event.getY(event.findPointerIndex(moveId)));
                            }
                            catch (IllegalArgumentException e) {
                                moveId = event.getPointerId(0);
                            }
                        }
                        if (shoot) {
                            try {
                                view.getPlayer().setShotEndX(event.getX(event.findPointerIndex(shotId)));
                                view.getPlayer().setShotEndY(event.getY(event.findPointerIndex(shotId)));
                            }
                            catch (IllegalArgumentException e) {
                                shotId = event.getPointerId(0);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        if (event.getActionIndex() == event.findPointerIndex(moveId)) {
                            view.getPlayer().setStartX(0);
                            view.getPlayer().setStartY(0);
                            view.getPlayer().setEndX(0);
                            view.getPlayer().setEndY(0);
                            move = false;
                            shotId = event.getPointerId(0);
                        }
                        else {
                            view.getPlayer().setShotStartX(0);
                            view.getPlayer().setShotStartY(0);
                            view.getPlayer().setShotEndX(0);
                            view.getPlayer().setShotEndY(0);
                            shoot = false;
                            moveId = event.getPointerId(0);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (move) {
                            view.getPlayer().setStartX(0);
                            view.getPlayer().setStartY(0);
                            view.getPlayer().setEndX(0);
                            view.getPlayer().setEndY(0);
                            move = false;
                        }
                        else {
                            view.getPlayer().setShotStartX(0);
                            view.getPlayer().setShotStartY(0);
                            view.getPlayer().setShotEndX(0);
                            view.getPlayer().setShotEndY(0);
                            shoot = false;
                        }
                        break;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometerMode) {
            mSensorManager.unregisterListener(this);
        }
        stopService(musicServiceIntent);
        finish();
        view = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(musicServiceIntent);
        finish();
        view = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(musicServiceIntent);
        view = null;
    }

    /**
     * Detects accelerometer changes, and acts respectively.
     * <p>
     * @param event The sensor event.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Track changes in accelerometer values and respond respectively
        if (accelerometerMode) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long actualTime = System.currentTimeMillis();

                if (actualTime - mLastUpdate > UPDATE_THRESHOLD) {
                    mLastUpdate = actualTime;

                    float x = event.values[0], y = event.values[1];

                    if (Math.abs(x) > accSensitivity || Math.abs(y) > accSensitivity) {
                        view.getPlayer().setStartX(0);
                        view.getPlayer().setStartY(0);
                        view.getPlayer().setEndY(x);
                        view.getPlayer().setEndX(y);
                    }
                    else {
                        view.getPlayer().setEndX(0);
                        view.getPlayer().setEndY(0);
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Restarts the game.
     */
    public void restartGame() {
        this.view = new MainView(this);
        setContentView(this.view);
    }


    /**
     * Returns to main menu by destroying the activity.
     */
    public void returnToMenu() {
        finish();
    }


    /**
     * Displays the game over dialog.
     */
    public void showGameOverDialog() {
        saveHighScores();
        DialogFragment dialogFragment = new GameOverDialogFragment();
        try {
            dialogFragment.show(getSupportFragmentManager(), "game_over");
        }
        finally {
        }
    }

    /**
     * Displays the level end dialog.
     */
    public void showLevelEndDialog() {
        levelEndDialogShown = true;
        DialogFragment dialogFragment = new LevelEndDialogFragment();
        try {
            dialogFragment.show(getSupportFragmentManager(), "level_complete");
        }
        finally {
        }
    }

    /**
     * Returns whether the level end dialog is shown.
     * <p>
     * @return True if the level end dialog is shown, otherwise false.
     */
    public boolean isLevelEndDialogShown() {
        return this.levelEndDialogShown;
    }

    /**
     * Set the value for the boolean that represents whether the level end dialog is shown.
     * <p>
     * @param levelEndDialogShown True if the level end dialog is shown, false otherwise.
     */
    public void setLevelEndDialogShown(boolean levelEndDialogShown) {
        this.levelEndDialogShown = levelEndDialogShown;
    }

    /**
     * Save high scores.
     */
    private void saveHighScores() {
        if (this.view.getManager().getLevel() > Store.readInt(getApplicationContext(), R.string.highest_level_key, 0)) {
            Store.saveInt(getApplicationContext(), R.string.highest_level_key, this.view.getManager().getLevel());
        }
        if (this.view.getManager().getKills() > Store.readInt(getApplicationContext(), R.string.most_kills_key, 0)) {
            Store.saveInt(getApplicationContext(), R.string.most_kills_key, this.view.getManager().getKills());
        }
        if (this.view.getManager().getScore() > Store.readInt(getApplicationContext(), R.string.highest_score_key, 0)) {
            Store.saveInt(getApplicationContext(), R.string.highest_score_key, this.view.getManager().getScore());
        }
    }

    /**
     * Get the message string for an increased value.
     * <p>
     * @return The string for increased value.
     */
    public String getIncreasedValueString() {
        return increasedValueString;
    }

    /**
     * Set the message for an increased value.
     * <p>
     * @param string The desire message.
     */
    public void setIncreasedValueString(String string) {
        this.increasedValueString = string;
    }

    /**
     * Gets the game manager object.
     * <p>
     * @return The manager object associated with the view.
     */
    public Manager getManager() {
        return this.view.getManager();
    }

    /**
     * Set the value for the new game field.
     * <p>
     * @param bool True if it is a new game, false if it is a loaded game.
     */
    public static void setNewGame(boolean bool) {
        newGame = bool;
    }

    /**
     * Get the value of the new game field.
     * <p>
     * @return True if new game, false if loaded game.
     */
    public static boolean getNewGame() {
        return newGame;
    }

    /**
     * Hides the navigation bar.
     */
    public void hideNavigation() {
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);
    }

    /**
     * Find the real display size (when navigation is hidden).
     * <p>
     * @return The display size when the navigation bar is hidden.
     */
    public Vector2 getRealDisplaySize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return new Vector2(size.x, size.y);
    }
}