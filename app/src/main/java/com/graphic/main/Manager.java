package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class Manager implements Runnable {
    private int level;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> playerProjectiles;
    private ArrayList<Projectile> enemyProjectiles;
    private Director director;
    private MainView view;
    private Paint bgLinePaint;
    private Paint bgPaint;
    private Thread thread;

    public Manager(MainView view) {
        level = 1;
        player = new Player(new Vector2(0, 0), 1f, 100f, 10f, view, this);
        enemies = new ArrayList<Enemy>();
        playerProjectiles = new ArrayList<Projectile>();
        enemyProjectiles = new ArrayList<Projectile>();
        this.view = view;

        this.bgLinePaint = new Paint();
        this.bgLinePaint.setStrokeWidth(3);
        this.bgLinePaint.setColor(0xa0ffffff);

        this.bgPaint = new Paint();
        this.bgPaint.setColor(0xff000000);

        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        while (true) {
            // Generate enemies using the director

            // While enemies exist, continue running the level
            while (!enemies.isEmpty()) {
                for (int i = 0; i < enemies.size(); i++) {
                    for (int j = 0; j < playerProjectiles.size(); j++) {
                        if (playerProjectiles.get(j) != null) {
                            if (enemies.get(i).detectHit(playerProjectiles.get(j))) {
                                enemies.get(i).damage(playerProjectiles.get(j).getDamage());
                                playerProjectiles.get(j).setHitTarget(true);
                                playerProjectiles.remove(j);
                                j--;
                            }
                        }
                    }
                    if (!enemies.get(i).isAlive()) {
                        enemies.remove(i);
                        i--;
                    }
                }
                for (int k = 0; k < enemyProjectiles.size(); k++) {
                    if (enemyProjectiles.get(k) != null) {
                        if (player.detectHit(enemyProjectiles.get(k))) {
                            player.damage(enemyProjectiles.get(k).getDamage());
                            enemyProjectiles.get(k).setHitTarget(true);
                            enemyProjectiles.remove(k);
                            k--;
                        }
                    }
                }
            }

            // Show prompt to continue to next level
        }
    }

    public void moveEnemies(Vector2 positionChange) {
        for (int i = 0; i < this.enemies.size(); i++) {
            this.enemies.get(i).changePosition(positionChange);
        }
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
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

    public Player getPlayer() {
        return this.player;
    }

    public void addPlayerProjectile(Projectile projectile) {
        playerProjectiles.add(projectile);
    }

    public void addEnemyProjectile(Projectile projectile) {
        enemyProjectiles.add(projectile);
    }
}
