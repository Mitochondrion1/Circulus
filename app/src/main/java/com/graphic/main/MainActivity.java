package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private MainView view;
    private Intent musicServiceIntent;
    private Vector2 displaySize;
    private boolean accelerometerMode;
    private boolean levelEndDialogShown;

    private static final int UPDATE_THRESHOLD = 50;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private long mLastUpdate;

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

        if (accelerometerMode) {
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            if (null == (mAccelerometer = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)))
                finish();
        }

        startService(musicServiceIntent);
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
        if (accelerometerMode) {
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

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometerMode) {
            mSensorManager.unregisterListener(this);
        }
        stopService(musicServiceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(musicServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(musicServiceIntent);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (accelerometerMode) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long actualTime = System.currentTimeMillis();

                if (actualTime - mLastUpdate > UPDATE_THRESHOLD) {
                    mLastUpdate = actualTime;

                    float x = event.values[0], y = event.values[1];

                    if (Math.abs(x) > 2 || Math.abs(y) > 2) {
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

    public void restart() {
        this.view = new MainView(this);
        setContentView(this.view);
    }

    public void returnToMenu() {
        finish();
    }

    public void showGameOverDialog() {
        DialogFragment dialogFragment = new GameOverDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "game_over");
    }

    public void showLevelEndDialog() {
        levelEndDialogShown = true;
        DialogFragment dialogFragment = new LevelEndDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "level_complete");
    }

    public boolean isLevelEndDialogShown() {
        return this.levelEndDialogShown;
    }

    public void setLevelEndDialogShown(boolean levelEndDialogShown) {
        this.levelEndDialogShown = levelEndDialogShown;
    }
}