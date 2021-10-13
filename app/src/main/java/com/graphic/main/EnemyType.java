package com.graphic.main;

public class EnemyType {
    private int directorCost;
    private float baseHealth;
    private float baseDamage;

    public EnemyType(int directorCost, float baseHealth, float baseDamage) {
        this.directorCost = directorCost;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
    }

    public int getDirectorCost() {
        return directorCost;
    }

    public float getBaseHealth() {
        return baseHealth;
    }

    public float getBaseDamage() {
        return baseDamage;
    }
}
