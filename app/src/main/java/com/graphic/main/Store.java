package com.graphic.main;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

public class Store {
    public static void saveInt(Context context, int key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(key), value);
        editor.commit();
    }

    public static int readInt(Context context, int key, int defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getInt(context.getString(key), defaultValue);
    }

    public static void saveBool(Context context, int key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(key), value);
        editor.commit();
    }

    public static boolean readBool(Context context, int key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getString(key), defaultValue);
    }
}
