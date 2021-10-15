package com.graphic.main;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

public class Store {
    public static void save(Activity activity, int key, int value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(key), value);
        editor.apply();
    }

    public static int read(Activity activity, int key, int defaultValue) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(activity.getString(key), defaultValue);
    }
}
