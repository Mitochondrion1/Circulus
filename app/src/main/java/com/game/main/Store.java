package com.game.main;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Contains methods to store and read from shared preferences.
 */
public class Store {
    /**
     * Stores an integer.
     * <p>
     * @param context   The context.
     * @param key       The shared preference key.
     * @param value     The value to store.
     */
    public static void saveInt(Context context, int key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(key), value);
        editor.commit();
    }

    /**
     * Reads an integer.
     * <p>
     * @param context       The context.
     * @param key           The shared preference key.
     * @param defaultValue  The default value to return.
     */
    public static int readInt(Context context, int key, int defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getInt(context.getString(key), defaultValue);
    }

    /**
     * Stores a long integer.
     * <p>
     * @param context   The context.
     * @param key       The shared preference key.
     * @param value     The value to store.
     */
    public static void saveLong(Context context, int key, long value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(context.getString(key), value);
        editor.commit();
    }

    /**
     * Reads a long integer.
     * <p>
     * @param context       The context.
     * @param key           The shared preference key.
     * @param defaultValue  The default value to return.
     */
    public static long readLong(Context context, int key, long defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getLong(context.getString(key), defaultValue);
    }

    /**
     * Stores a boolean.
     * <p>
     * @param context   The context.
     * @param key       The shared preference key.
     * @param value     The value to store.
     */
    public static void saveBool(Context context, int key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(key), value);
        editor.commit();
    }

    /**
     * Reads a boolean.
     * <p>
     * @param context       The context.
     * @param key           The shared preference key.
     * @param defaultValue  The default value to return.
     */
    public static boolean readBool(Context context, int key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getString(key), defaultValue);
    }

    /**
     * Stores a float.
     * <p>
     * @param context   The context.
     * @param key       The shared preference key.
     * @param value     The value to store.
     */
    public static void saveFloat(Context context, int key, float value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(context.getString(key), value);
        editor.commit();
    }

    /**
     * Reads a float.
     * <p>
     * @param context       The context.
     * @param key           The shared preference key.
     * @param defaultValue  The default value to return.
     */
    public static float readFloat(Context context, int key, float defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        return sharedPref.getFloat(context.getString(key), defaultValue);
    }
}
