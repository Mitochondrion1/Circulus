package com.game.main;

/**
 * Defines a 2-dimensional vector object, with float coordinates.
 */
public class Vector2 {
    // Load native-lib.cpp
    static {
        System.loadLibrary("native-lib");
    }

    /** The x coordinate. */
    private float x;
    /** The y coordinate. */
    private float y;

    /**
     * Constructs a Vector2 object.
     * <p>
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate.
     * <p>
     * @return The x coordinate of the vector.
     */
    public float getX() {
        return x;
    }

    /**
     * Set a new x coordinate for the vector.
     * <p>
     * @param x The new value.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the y coordinate.
     * <p>
     * @return The y coordinate of the vector.
     */
    public float getY() {
        return y;
    }

    /**
     * Set a new y coordinate for the vector.
     * <p>
     * @param y The new value.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Calculates the length of the vector.
     * <p>
     * @return The length of the vector.
     */
    public float getLength() {
        return (float)Math.sqrt(x * x + y * y);
    }

    /**
     * Set the length of the vector to a certain length.
     * <p>
     * @param length The desired length.
     */
    public void setLength(float length) {
        // New length calculation uses the algorithm from native-lib
        float multiplier = length * quickInvSqrt(this.x * this.x + this.y * this.y);
        this.x *= multiplier;
        this.y *= multiplier;
    }

    /**
     * Calculates the distance between two given points.
     * <p>
     * @param v1    One point.
     * @param v2    Another point.
     * @return      The distance between the points.
     */
    public static float distance(Vector2 v1, Vector2 v2) {
        return (float)Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y));
    }

    /**
     * Subtracts 2 vectors.
     * <p>
     * @param v1    One vector.
     * @param v2    Another Vector.
     * @return      The subtraction of the first vector from the second one.
     */
    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v2.x - v1.x, v2.y - v1.y);
    }

    /**
     * Get a string representing the vector.
     * <p>
     * @return The vector string in a (x,y) format.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * C++ fast inverse square root algorithm implementation.
     * <p><
     * @param lengthSquared The length of the vector squared.
     * @return              The inverse of the length.
     */
    public native float quickInvSqrt(float lengthSquared);
}
