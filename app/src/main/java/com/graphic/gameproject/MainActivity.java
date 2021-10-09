package com.graphic.gameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    private MainView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        view = new MainView(this);
        setContentView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.getPlayer().setStartX(event.getX());
                view.getPlayer().setStartY(event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                view.getPlayer().setEndX(event.getX());
                view.getPlayer().setEndY(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                view.getPlayer().setStartX(0);
                view.getPlayer().setStartY(0);
                view.getPlayer().setEndX(0);
                view.getPlayer().setEndY(0);
                break;
        }
        return true;
    }
}