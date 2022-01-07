package com.henleylee.lockpattern;

import androidx.annotation.NonNull;

/**
 * {@link Cell}信息
 *
 * @author Henley
 * @since 2019/8/26 16:39
 */
public class Cell {

    private int index;          // the cell index
    private float x;            // the x position of circle's center point
    private float y;            // the y position of circle's center point
    private float radius;       // the cell radius
    private int status;         // the cell status

    public Cell(int index, float x, float y, float radius, int status) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cell{" +
                "index=" + index +
                ", x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", status=" + status +
                '}';
    }

}
