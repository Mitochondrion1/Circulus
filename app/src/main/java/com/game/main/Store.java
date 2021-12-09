package com.game.main;

import android.content.Context;
import android.content.SharedPreferences;

// Contains static methods to store values into shared preferences
public class Store {
    // Save an integer
    public static void saveInt(Context context, int key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(key), value);
        editor.commit();
    }

    // Retrieve an integer
    public static int readInt(Context context, int key, int defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getInt(context.getString(key), defaultValue);
    }

    public static void saveLong(Context context, int key, long value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(context.getString(key), value);
        editor.commit();
    }

    public static long readLong(Context context, int key, long defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getLong(context.getString(key), defaultValue);
    }

    // Save a boolean
    public static void saveBool(Context context, int key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(key), value);
        editor.commit();
    }

    // Retrieve a boolean
    public static boolean readBool(Context context, int key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getString(key), defaultValue);
    }

    // Save a string
    public static void saveString(Context context, int key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(key), value);
        editor.commit();
    }

    // Retrieve a string
    public static String readString(Context context, int key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(key), defaultValue);
    }
}
