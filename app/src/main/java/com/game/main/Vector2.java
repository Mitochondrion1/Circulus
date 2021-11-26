package com.game.main;

// Represents a 2-dimensional vector
public class Vector2 {
    // Load native-lib.cpp
    static {
        System.loadLibrary("native-lib");
    }

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

    // Get the length of the vector
    public float getLength() {
        return (float)Math.sqrt(x * x + y * y);
    }

    // Change the length of the vector to a specific value while the direction is kept
    public void setLength(float length) {
        // New length calculation uses the algorithm from native-lib
        float multiplier = length * quickInvSqrt(this.x * this.x + this.y * this.y);
        this.x *= multiplier;
        this.y *= multiplier;
    }

    // Find the distance between tow points
    public static float distance(Vector2 v1, Vector2 v2) {
        return (float)Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y));
    }

    // Subtract two vectors
    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v2.x - v1.x, v2.y - v1.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // Implement the fast inverse square root from native-lib.cpp
    public native float quickInvSqrt(float length);
}