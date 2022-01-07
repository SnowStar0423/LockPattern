package com.henleylee.lockpattern;

import androidx.annotation.ColorInt;

/**
 * Style(颜色、描边等信息)
 *
 * @author Henley
 * @since 2019/8/26 17:05
 */
public class DecoratorStyle {

    @ColorInt
    private int normalColor;
    @ColorInt
    private int fillColor;
    @ColorInt
    private int checkColor;
    @ColorInt
    private int errorColor;

    private float strokeWidth;
    private float lineWidth;

    public DecoratorStyle(int normalColor, int checkColor, int errorColor, int fillColor, float strokeWidth, float lineWidth) {
        this.normalColor = normalColor;
        this.checkColor = checkColor;
        this.errorColor = errorColor;
        this.fillColor = fillColor;
        this.strokeWidth = strokeWidth;
        this.lineWidth = lineWidth;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(int checkColor) {
        this.checkColor = checkColor;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(int errorColor) {
        this.errorColor = errorColor;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @ColorInt
    public int getColor(@CellStatus int status) {
        if (status == CellStatus.ERROR) {
            return getErrorColor();
        } else if (status == CellStatus.CHECK) {
            return getCheckColor();
        } else {
            return getNormalColor();
        }
    }

}
