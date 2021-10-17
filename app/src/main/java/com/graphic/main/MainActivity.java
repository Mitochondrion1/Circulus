package com.graphic.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    private MainView view;
    private Intent musicServiceIntent;
    private Vector2 displaySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);
        view = new MainView(this);
        displaySize = DisplayParams.getDisplaySize(this);
        startService(musicServiceIntent);
        setContentView(view);
    }

    private boolean move, shoot;
    private int moveId, shotId;
    @Override
    public boolean onTouchEvent(MotionEvent event) { ;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() >= displaySize.getX() / 2) {
                    view.getPlayer().setStartX(event.getX());
                    view.getPlayer().setStartY(event.getY());
                    view.getPlayer().setEndX(event.getX());
                    view.getPlayer().setEndY(event.getY());
                    move = true;
                    moveId = event.getPointerId(event.getActionIndex());
                }
                else {
                    view.getPlayer().setShotStartX(event.getX());
                    view.getPlayer().setShotStartY(event.getY());
                    view.getPlayer().setShotEndX(event.getX());
                    view.getPlayer().setShotEndY(event.getY());
                    view.getPlayer().setTick(0);
                    shoot = true;
                    shotId = event.getPointerId(event.getActionIndex());
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getActionIndex() >= displaySize.getX() / 2 && !move) {
                    view.getPlayer().setStartX(event.getX(event.getActionIndex()));
                    view.getPlayer().setStartY(event.getY(event.getActionIndex()));
                    view.getPlayer().setEndX(event.getX(event.getActionIndex()));
                    view.getPlayer().setEndY(event.getY(event.getActionIndex()));
                    move = true;
                    moveId = event.getPointerId(event.getActionIndex());
                }
                else if (event.getActionIndex() >= displaySize.getX() / 2  && !shoot) {
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
                    view.getPlayer().setEndX(event.getX(event.findPointerIndex(moveId)));
                    view.getPlayer().setEndY(event.getY(event.findPointerIndex(moveId)));
                }
                if (shoot) {
                    view.getPlayer().setShotEndX(event.getX(event.findPointerIndex(shotId)));
                    view.getPlayer().setShotEndY(event.getY(event.findPointerIndex(shotId)));
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getActionIndex() == event.findPointerIndex(moveId)) {
                    view.getPlayer().setStartX(0);
                    view.getPlayer().setStartY(0);
                    view.getPlayer().setEndX(0);
                    view.getPlayer().setEndY(0);
                }
                else {
                    view.getPlayer().setShotStartX(0);
                    view.getPlayer().setShotStartY(0);
                    view.getPlayer().setShotEndX(0);
                    view.getPlayer().setShotEndY(0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.findPointerIndex(moveId) == 0) {
                    view.getPlayer().setStartX(0);
                    view.getPlayer().setStartY(0);
                    view.getPlayer().setEndX(0);
                    view.getPlayer().setEndY(0);
                }
                else {
                    view.getPlayer().setShotStartX(0);
                    view.getPlayer().setShotStartY(0);
                    view.getPlayer().setShotEndX(0);
                    view.getPlayer().setShotEndY(0);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}