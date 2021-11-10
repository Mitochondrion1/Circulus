package com.graphic.main;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

// Contains methods to retrieve the display metrics based on a view or context
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
