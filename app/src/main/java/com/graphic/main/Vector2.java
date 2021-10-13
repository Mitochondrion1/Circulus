package com.graphic.main;

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

    public static float distance(Vector2 v1, Vector2 v2) {
        return (float)Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y * v2.y));
    }

    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v2.x - v1.x, v2.y - v1.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
