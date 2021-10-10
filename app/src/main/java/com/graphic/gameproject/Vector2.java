package com.graphic.gameproject;

import androidx.annotation.NonNull;

public class Vector2 {
    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
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

    public float getLength() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public void setLength(float length) {
        float l = this.getLength();
        this.x *= length / l;
        this.y *= length / l;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
