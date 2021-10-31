package com.graphic.main;

import java.util.Random;

public class Director {
    private Manager manager;
    private EnemyType[] enemyTypes;
    private int credits;
    private int initialCredits;

    public Director(Manager manager) {
        this.manager = manager;
        this.enemyTypes = new EnemyType[]
                {EnemyTypeData.EXPLODER, EnemyTypeData.SHOOTER,
                EnemyTypeData.LASERMAN, EnemyTypeData.SUMMONER};
    }

    public void setCredits(int credits) {
        this.credits = credits;
        this.initialCredits = this.credits;
    }

    public void generateEnemies() {
        float[] values = new float[this.enemyTypes.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = generateValue(this.enemyTypes[i]);
        }

        int mostExpensive = this.enemyTypes.length - 1;
        float sum;
        float generatedNum;
        Random random = new Random();
        while (mostExpensive >= 0) {
            for (int i = this.enemyTypes.length - 1; i >= 0; i--) {
                if (mostExpensive >= 0 && this.enemyTypes[i].getDirectorCost() > this.credits) {
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
                }
            }
        }
    }

    private float generateValue(EnemyType enemyType) {
        float constant = 0.2f;
        return (float)Math.pow(constant / enemyType.getDirectorCost() * this.initialCredits, 0.1 * enemyType.getDirectorCost());
    }

    private void normalizeValues(float[] values, int x) {
        float sum = 0f;
        for (int i = 0; i < x; i++) {
            sum += values[i];
        }
        for (int j = 0; j < x; j++) {
            values[j] *= 1f / sum;
        }
    }

    private void addEnemy(int num) {
        Vector2 position;
        Random random = new Random();
        position = new Vector2(random.nextFloat() * 20 - 10f, random.nextFloat() * 15 - 7.5f);
        switch (num) {
            case 0:
                this.manager.addEnemy(new Exploder(position, this.manager, this.manager.getView()));
                this.credits -= enemyTypes[0].getDirectorCost();
                break;
            case 1:
                this.manager.addEnemy(new Shooter(position, this.manager, this.manager.getView()));
                this.credits -= enemyTypes[1].getDirectorCost();
                break;
            case 2:
                this.manager.addEnemy(new Laserman(position, this.manager, this.manager.getView()));
                this.credits -= enemyTypes[2].getDirectorCost();
                break;
            case 3:
                this.manager.addEnemy(new Summoner(position, this.manager, this.manager.getView()));
                this.credits -= enemyTypes[3].getDirectorCost();
                break;
        }
    }
}
