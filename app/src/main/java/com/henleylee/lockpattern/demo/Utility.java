package com.henleylee.lockpattern.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

/**
 * Toast工具类
 */
public class Utility {

    private static final String PREFERENCES_NAME = "config_pref";
    private static final String PATTERN_PASSWORD = "pattern_password";

    private static Toast sToast;

    @SuppressLint("ShowToast")
    public static void showToast(@NonNull Context context, @Nullable CharSequence text) {
        if (text == null || TextUtils.isEmpty(text)) {
            return;
        }
        int duration = Toast.LENGTH_SHORT;
        if (text.length() > 15) {
            duration = Toast.LENGTH_LONG;
        }
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), text, duration);
        } else {
            sToast.setText(text);
        }
        sToast.show();
    }

    /**
     * 初始化ActionBar
     */
    public static void initActionBar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);     // Set whether to title/subtitle (default is true)
            actionBar.setHomeButtonEnabled(true);           // Set whether to enable the icon on the left to be clickable (default is false)
            actionBar.setDisplayShowHomeEnabled(true);      // Set whether to display the application icon (default is true)
            actionBar.setDisplayHomeAsUpEnabled(true);      // Set whether to add a return icon to the left side (default is false)
        }
    }

    public static String getPatternPassword(@NonNull Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(PATTERN_PASSWORD, null);
    }

    public static void savePatternPassword(@NonNull Context context, @Nullable String password) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(PATTERN_PASSWORD, password)
                .apply();
    }

}
