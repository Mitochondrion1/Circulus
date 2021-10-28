package com.graphic.main;

import java.util.Random;

public class Director {
    private Manager manager;
    private EnemyType[] enemyTypes;
    private int credits;

    public Director(Manager manager) {
        this.manager = manager;
        this.enemyTypes = new EnemyType[]
                {EnemyTypeData.EXPLODER, EnemyTypeData.SHOOTER,
                EnemyTypeData.LASERMAN, EnemyTypeData.SUMMONER};
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void generateEnemies() {
        int mostExpensive = this.enemyTypes.length - 1;
        int total, sum;
        int generatedNum;
        Random random = new Random();
        while (mostExpensive >= 0) {
            for (int i = this.enemyTypes.length - 1; i >= 0; i--) {
                if (mostExpensive >= 0 && this.enemyTypes[i].getDirectorCost() > this.credits) {
                    mostExpensive--;
                }
            }
            total = 0;
            for (int j = 0; j <= mostExpensive; j++) {
                total += enemyTypes[j].getDirectorCost();
            }
            generatedNum = random.nextInt(total + 1);

            sum = 0;
            for (int k = 0; k <= mostExpensive; k++) {
                sum += enemyTypes[k].getDirectorCost();
                if (generatedNum < sum) {
                    this.addEnemy(k);
                }
            }
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
