package com.graphic.main;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

public class Store {
    public static void save(Context context, int key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(key), value);
        editor.commit();
    }

    public static int read(Context context, int key, int defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getInt(context.getString(key), defaultValue);
    }
}
