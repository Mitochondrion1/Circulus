package com.graphic.gameproject;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class DisplayParams {
    public static Vector2 getDisplaySize(View view) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new Vector2(dm.widthPixels, dm.heightPixels);
    }

    public static Vector2 getDisplaySize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new Vector2(dm.widthPixels, dm.heightPixels);
    }
}
