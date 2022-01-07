package com.henleylee.lockpattern;

import android.graphics.Color;

import androidx.annotation.ColorInt;

/**
 * Config
 *
 * @author Henley
 * @since 2019/8/26 18:04
 */
final class Config {

    @ColorInt
    static final int DEFAULT_NORMAL_COLOR = Color.parseColor("#108EE9");
    @ColorInt
    static final int DEFAULT_CHECK_COLOR = Color.parseColor("#108EE9");
    @ColorInt
    static final int DEFAULT_ERROR_COLOR = Color.parseColor("#F44336");
    @ColorInt
    static final int DEFAULT_FILL_COLOR = Color.parseColor("#FFFFFF");

    static final int DEFAULT_SIDE = 3;
    static final int DEFAULT_FREEZE_DURATION = 1000; // ms

}