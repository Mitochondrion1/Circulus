package com.game.main;

import java.util.Random;

/**
 * The object responsible for enemy generation at the beginning of each level
 */
public class Director {
    /** The manager object associated with the director. */
    private Manager manager;
    /**
     * Costs of each enemy:
     * costs[0] - The cost of a Shooter enemy.
     * costs[1] - The cost of an Exploder enemy.
     * costs[2] - The cost of a Laserman enemy.
     * costs[3] - The cost of a Summoner enemy.
     */
    private final int[] costs = {5, 10, 15, 20};
    /** The amount of credits the director can use. */
    private int credits;
    /** The amount of credits given to the director initially. Updates each level. */
    private int initialCredits;

    /**
     * Constructs a Director object.
     * @param manager The manager to associate with the director.
     */
    public Director(Manager manager) {
        this.manager = manager;
    }

    /**
     * Set the amount of credits the director can use for enemy generation.
     * <p>
     * @param credits The amount of credits to give the director.
     */
    public void setCredits(int credits) {
        this.credits = credits;
        this.initialCredits = this.credits;
    }

    /**
     * The method that generates the enemies.
     */
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

    /**
     * Gives each enemy type a value
     * <p>
     * @param cost  The cost of the enemy.
     * @return      A value for a given enemy.
     */
    private float generateValue(int cost) {
        float constant = 0.3f;
        return (float)Math.pow(constant / cost * this.initialCredits, 0.05 * cost);
    }

    /**
     * Changes the values for enemies, so that the sum of all values for enemies that can be spawned is 1.
     * <p>
     * @param values    The values generated for each enemy type by the {@link #generateValue(int)} method.
     * @param x         The index of the last enemy to normalize the values for.
     */
    private void normalizeValues(float[] values, int x) {
        float sum = 0f;
        for (int i = 0; i <= x; i++) {
            sum += values[i];
        }
        for (int j = 0; j <= x; j++) {
            values[j] *= 1f / sum;
        }
    }

    /**
     * Adds an enemy based on a generated number.
     * <p>
     * @param num The number of the enemy: 0 - Shooter; 1 - Exploder; 2 - Laserman; 3 - Summoner.
     */
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
