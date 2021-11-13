package com.graphic.main;

import android.util.Log;

import java.util.Random;

// The object responsible for enemy generation at the beginning of each level
public class Director {
    private Manager manager;
    private final int[] costs = {5, 10, 15, 20};
    private int credits;
    private int initialCredits;

    public Director(Manager manager) {
        this.manager = manager;
        Log.d("Costs", String.valueOf(costs[0]) + costs[1] + costs[2] + costs[3]);
    }

    // Set the amount of credits the director can use for enemy generation
    public void setCredits(int credits) {
        this.credits = credits;
        this.initialCredits = this.credits;
    }

    // The method that generates the enemies
    public void generateEnemies() {
        /*
         * Each enemy type gets a values based on its director cost and the initial
         * amount of director credits, using a mathematical function
         */
        float[] values = new float[this.costs.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = generateValue(this.costs[i]);
        }

        int mostExpensive = this.costs.length - 1;
        float sum;
        float generatedNum;
        Random random = new Random();

        // Add a random enemy based on the values generated earlier
        while (mostExpensive >= 0) {
            for (int i = this.costs.length - 1; i >= 0; i--) {
                if (mostExpensive >= 0 && this.costs[i] > this.credits) {
                    mostExpensive--;
                }
            }
            normalizeValues(values, mostExpensive);

            generatedNum = random.nextFloat();

            sum = 0f;
            for (int k = 0; k <= mostExpensive; k++) {
                sum += values[k];
                if (generatedNum < sum) {
                    this.addEnemy(k);
                    break;
                }
            }
        }
    }

    // Gives each enemy type a value
    private float generateValue(int cost) {
        float constant = 0.05f;
        return (float)Math.pow(constant / cost * this.initialCredits, 0.1 * cost);
    }

    // Changes the values for enemies, so that the sum of all values for enemies that can be spawned is 1
    private void normalizeValues(float[] values, int x) {
        float sum = 0f;
        for (int i = 0; i < x; i++) {
            sum += values[i];
        }
        for (int j = 0; j < x; j++) {
            values[j] *= 1f / sum;
        }
    }

    // Adds an enemy based on a generated number
    private void addEnemy(int num) {
        Vector2 position;
        Random random = new Random();
        position = new Vector2(random.nextFloat() * (manager.getBoundRight() - manager.getBoundLeft()) + manager.getBoundLeft(),
                random.nextFloat() * (manager.getBoundBottom() - manager.getBoundTop()) + manager.getBoundTop());
        switch (num) {
            case 0:
                this.manager.addEnemy(new Shooter(position, this.manager, this.manager.getView()));
                this.credits -= costs[0];
                break;
            case 1:
                Exploder exploder = new Exploder(position, this.manager, this.manager.getView());
                this.manager.addEnemy(exploder);
                this.credits -= costs[1];
                break;
            case 2:
                this.manager.addEnemy(new Laserman(position, this.manager, this.manager.getView()));
                this.credits -= costs[2];
                break;
            case 3:
                this.manager.addEnemy(new Summoner(position, this.manager, this.manager.getView()));
                this.credits -= costs[3];
                break;
        }
    }
}
