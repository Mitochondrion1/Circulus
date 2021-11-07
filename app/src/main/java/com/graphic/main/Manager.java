package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Manager implements Runnable {
    private int level;
    private int kills;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> playerProjectiles;
    private ArrayList<Projectile> enemyProjectiles;
    private ArrayList<HealthPack> healthPacks;
    private Director director;
    private MainView view;
    private Paint bgLinePaint;
    private Paint bgPaint;
    private Paint arrowPaint;
    private Thread thread;
    private Random random;

    public Manager(MainView view) {
        level = 1;
        kills = 0;
        player = new Player(new Vector2(0, 0), view, this);
        enemies = new ArrayList<>();
        playerProjectiles = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();
        healthPacks = new ArrayList<>();
        this.view = view;
        this.director = new Director(this);

        this.bgLinePaint = new Paint();
        this.bgLinePaint.setStrokeWidth(3);
        this.bgLinePaint.setColor(0xa0ffffff);

        this.bgPaint = new Paint();
        this.bgPaint.setColor(0xff000000);

        this.arrowPaint = new Paint();
        this.arrowPaint.setColor(0xffffffff);

        this.random = new Random();

        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        int maxHealthPacks = 5;
        int rand;
        while (player.isAlive()) {
            // Generate enemies using the director
            director.setCredits(50 + 50 * level);
            director.generateEnemies();
            rand = random.nextInt(3);

            // While enemies exist, continue running the level
            while (!enemies.isEmpty()) {
                for (int i = 0; i < enemies.size(); i++) {
                    for (int j = 0; j < playerProjectiles.size(); j++) {
                        if (playerProjectiles.get(j) != null) {
                            if (enemies.get(i) != null && enemies.get(i).detectHit(playerProjectiles.get(j))) {
                                enemies.get(i).damage(playerProjectiles.get(j).getDamage());
                                playerProjectiles.get(j).setHitTarget(true);
                            }
                            if (playerProjectiles.get(j).isHitTarget()) {
                                playerProjectiles.remove(j);
                                j--;
                            }
                        }
                    }
                    if (enemies.get(i) != null && !enemies.get(i).isAlive()) {
                        enemies.remove(i);
                        i--;
                        this.kills++;
                    }
                }
                for (int k = 0; k < enemyProjectiles.size(); k++) {
                    if (enemyProjectiles.get(k) != null) {
                        if (player.detectHit(enemyProjectiles.get(k))) {
                            player.damage(enemyProjectiles.get(k).getDamage());
                            enemyProjectiles.get(k).setHitTarget(true);
                        }
                        if (enemyProjectiles.get(k).isHitTarget()) {
                            enemyProjectiles.remove(k);
                            k--;
                        }
                    }
                }
                for (int l = 0; l < healthPacks.size(); l++) {
                    if (healthPacks.get(l) != null) {
                        if (Vector2.distance(this.player.getPosition(), this.healthPacks.get(l).getPosition()) <=
                                (this.player.getSize() + this.healthPacks.get(l).getSize()) / 2) {
                            this.player.heal(this.healthPacks.get(l).getHealAmount());
                            this.healthPacks.remove(l);
                            l--;
                        }
                    }
                }

                if (this.healthPacks.size() < maxHealthPacks && this.random.nextInt(1000) == 0) {
                    addHealthPack();
                }

                if (!player.isAlive()) {
                    break;
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Continue to next level
            if (player.isAlive()) {
                switch (rand) {
                    case 0:
                        this.player.increaseHealth();
                        this.view.getActivity().setIncreasedValueString("Your health has increased");
                        break;
                    case 1:
                        this.player.increaseDamage();
                        this.view.getActivity().setIncreasedValueString("Your damage has increased");
                        break;
                    case 2:
                        this.player.increaseAttackSpeed();
                        this.view.getActivity().setIncreasedValueString("Your attack speed has increased");
                        break;
                }
                this.view.getActivity().showLevelEndDialog();
                while (this.view.getActivity().isLevelEndDialogShown()) {
                }
                this.level++;
            }
        }
        this.view.getActivity().showGameOverDialog();
    }

    public ArrayList<HealthPack> getHealthPacks() {
        return healthPacks;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    private void addHealthPack() {
        Vector2 pos = new Vector2(this.random.nextFloat() * 20 - 10f, this.random.nextFloat() * 15 - 7.5f);
        this.healthPacks.add(new HealthPack(pos, this.view));
    }

    public void drawEnemies(Canvas canvas) {
        for (int i = 0; i < this.enemies.size(); i++) {
            this.enemies.get(i).draw(canvas);
        }
    }

    public void drawProjectiles(Canvas canvas) {
        for (int i = 0; i < playerProjectiles.size(); i++) {
            playerProjectiles.get(i).draw(canvas);
        }
        for (int j = 0; j < enemyProjectiles.size(); j++) {
            enemyProjectiles.get(j).draw(canvas);
        }
    }

    public void drawHealthPacks(Canvas canvas) {
        for (int i = 0; i < healthPacks.size(); i++) {
            healthPacks.get(i).draw(canvas);
        }
    }

    public void drawBackground(Canvas canvas) {
        float x = player.getPosition().getX(), y = player.getPosition().getY();
        float heightInUnits = this.view.getDisplaySize().getY() / this.view.getDisplaySize().getX() *
                this.view.getWidthInUnits();
        int horizontalLines = (int)Math.ceil(heightInUnits * 2);
        int verticalLines = (int)Math.ceil(this.view.getWidthInUnits() * 2);
        float distBetweenLines = 0.5f * this.view.getPixelsPerUnit();
        float beginHorizontal = this.view.getDisplaySize().getY() / 2 + (0.5f - y % 0.5f) * (distBetweenLines * 2);
        float beginVertical = this.view.getDisplaySize().getX() / 2 + (0.5f - x % 0.5f) * (distBetweenLines * 2);

        canvas.drawRect(0f, 0f, this.view.getDisplaySize().getX(), this.view.getDisplaySize().getY(), bgPaint);
        for (int i = 0; i < horizontalLines / 2 + 1; i++) {
            canvas.drawLine(0f, beginHorizontal + distBetweenLines * i,
                    this.view.getDisplaySize().getX(), beginHorizontal + distBetweenLines * i, bgLinePaint);
            canvas.drawLine(0f, beginHorizontal - distBetweenLines * (i + 1),
                    this.view.getDisplaySize().getX(), beginHorizontal - distBetweenLines * (i + 1), bgLinePaint);
        }
        for (int j = 0; j < verticalLines / 2 + 1; j++) {
            canvas.drawLine(beginVertical + distBetweenLines * j,
                    0f, beginVertical + distBetweenLines * j,
                    this.view.getDisplaySize().getY(), bgLinePaint);
            canvas.drawLine(beginVertical - distBetweenLines * (j + 1),
                    0f, beginVertical - distBetweenLines * (j + 1),
                    this.view.getDisplaySize().getY(), bgLinePaint);
        }
    }

    public int getLevel() {
        return level;
    }

    public int getKills() {
        return kills;
    }

    public Player getPlayer() {
        return this.player;
    }

    public MainView getView() {
        return view;
    }

    public int getEnemiesLeft() {
        return this.enemies.size();
    }

    public void addPlayerProjectile(Projectile projectile) {
        playerProjectiles.add(projectile);
    }

    public void addEnemyProjectile(Projectile projectile) {
        enemyProjectiles.add(projectile);
    }
}
