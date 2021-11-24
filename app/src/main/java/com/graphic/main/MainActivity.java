package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

// The main game activity
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private MainView view;
    private Intent musicServiceIntent;
    private Vector2 displaySize;
    private boolean accelerometerMode;
    private boolean levelEndDialogShown;
    private String increasedValueString;

    // Accelerometer parameters
    int accSensitivity;
    private static final int UPDATE_THRESHOLD = 15;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private long mLastUpdate;

    private static boolean newGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);
        view = new MainView(this);
        displaySize = DisplayParams.getDisplaySize(this);
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

    private boolean move = false, shoot = false;
    private int moveId, shotId;
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
        finish();
        view = null;
    }

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

    // Restart the game by replacing the view with a new one
    public void restart() {
        this.view = new MainView(this);
        setContentView(this.view);
    }

    // Destroy the activity and return to menu
    public void returnToMenu() {
        finish();
    }

    // Show the game over dialog
    public void showGameOverDialog() {
        saveHighScores();
        DialogFragment dialogFragment = new GameOverDialogFragment();
        try {
            dialogFragment.show(getSupportFragmentManager(), "game_over");
        }
        finally {
        }
    }

    // Show the level end dialog
    public void showLevelEndDialog() {
        levelEndDialogShown = true;
        DialogFragment dialogFragment = new LevelEndDialogFragment();
        try {
            dialogFragment.show(getSupportFragmentManager(), "level_complete");
        }
        finally {
        }
    }

    // Is the level end dialog still shown
    public boolean isLevelEndDialogShown() {
        return this.levelEndDialogShown;
    }

    // Set the value for the boolean that represents whether the level end dialog is shown
    public void setLevelEndDialogShown(boolean levelEndDialogShown) {
        this.levelEndDialogShown = levelEndDialogShown;
    }

    // Save high scores
    private void saveHighScores() {
        if (this.view.getManager().getLevel() > Store.readInt(getApplicationContext(), R.string.highest_level_key, 0)) {
            Store.saveInt(getApplicationContext(), R.string.highest_level_key, this.view.getManager().getLevel());
        }
        if (this.view.getManager().getKills() > Store.readInt(getApplicationContext(), R.string.most_kills_key, 0)) {
            Store.saveInt(getApplicationContext(), R.string.most_kills_key, this.view.getManager().getKills());
        }
    }

    // Get the message string for an increased value
    public String getIncreasedValueString() {
        return increasedValueString;
    }

    // Set the message  for an increased value
    public void setIncreasedValueString(String string) {
        this.increasedValueString = string;
    }

    public Manager getManager() {
        return this.view.getManager();
    }

    public static void setNewGame(boolean bool) {
        newGame = bool;
    }

    public static boolean getNewGame() {
        return newGame;
    }
}