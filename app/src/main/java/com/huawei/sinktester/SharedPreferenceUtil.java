package com.huawei.sinktester;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceUtil {
    public static final String PREF_FILE = "castplus";
    public static final String PREF_KEY_DISCOVERABLE = "discoverable";
    public static final String PREF_KEY_AUTH_MODE = "auth_mode";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_FPS = "fps";
    public static final String PREF_KEY_height = "height";
    public static final String PREF_KEY_width = "width";
    public static final String PREF_KEY_ROTATION = "rotation";

    public static final int DEFAULT_FPS = 60;

    public static final String DEFAULT_PASSWORD = Utils.randomCode();
    private static final String TAG = "SharedPreferenceUtil";

    public static void setDiscoverable(Context context, boolean discoverable) {
        Log.d(TAG, "setDiscoverable() called, discoverable: " + discoverable);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_DISCOVERABLE, discoverable);
        editor.commit();
    }

    public static boolean getDiscoverable(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        if (prefs == null) {
            setDiscoverable(context, true);
            return true;
        } else {
            return prefs.getBoolean(PREF_KEY_DISCOVERABLE, true);
        }
    }

    public static void setAuthMode(Context context, boolean passwordMode) {
        Log.d(TAG, "setAuthMode() called, passwordMode: " + passwordMode);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_AUTH_MODE, passwordMode);
        editor.commit();
    }

    public static boolean getAuthMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        if (prefs == null) {
            setAuthMode(context, false);
            return false;
        } else {
            return prefs.getBoolean(PREF_KEY_AUTH_MODE, false);
        }
    }

    public static void setPassword(Context context, String password) {
        Log.d(TAG, "setPassword() called, password: " + password);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_PASSWORD, password);
        editor.commit();
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        String password = prefs.getString(PREF_KEY_PASSWORD, DEFAULT_PASSWORD);
        if (DEFAULT_PASSWORD.equals(password)) {
            setPassword(context, DEFAULT_PASSWORD);
        }
        return password;
    }

    public static void setFps(Context context, int fps) {
        Log.d(TAG, "setPrefKeyFps() called, fps: " + fps);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_KEY_FPS, fps);
        editor.commit();
    }

    public static int getFps(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        int fps = prefs.getInt(PREF_KEY_FPS, DEFAULT_FPS);
        if (fps==DEFAULT_FPS) {
            setFps(context, DEFAULT_FPS);
        }
        return fps;
    }
    public static void setHeight(Context context, int height) {
        Log.d(TAG, "setHeight() called, Height: " + height);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_KEY_height, height);
        editor.commit();
    }
    public static int getHeight(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        int height = prefs.getInt(PREF_KEY_height, 1080);
        if (height==1080) {
            setHeight(context, 1080);
        }
        return height;
    }
    public static void setWidth(Context context, int width) {
        Log.d(TAG, "setWidth() called, Width: " + width);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_KEY_width, width);
        editor.commit();
    }
    public static int getWidth(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        int width = prefs.getInt(PREF_KEY_width, 1920);
        if (width==1920) {
            setWidth(context, 1920);
        }
        return width;
    }
    /*
    rotation:true为默认方向
    false 旋转
    * */
    public static void setRotation(Context context, boolean rotation) {
        Log.d(TAG, "setRotation() called, Rotation: " + rotation);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_ROTATION, rotation);
        editor.commit();
    }
    public static boolean getRotation(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        boolean rotation = prefs.getBoolean(PREF_KEY_ROTATION, true);
        if (rotation==true) {
            setRotation(context, true);
        }
        return rotation;
    }





}
