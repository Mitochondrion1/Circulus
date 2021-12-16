package com.game.main;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

// The object that is responsible for running the game world
public class Manager implements Runnable {
    private int level;
    private int kills;

    // Map bounds parameters
    private float boundLeft;
    private float boundTop;
    private float boundRight;
    private float boundBottom;
    private Paint boundPaint;

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> playerProjectiles;
    private ArrayList<Projectile> enemyProjectiles;
    private ArrayList<HealthPack> healthPacks;
    private Director director;
    private Timer timer;
    private MainView view;
    private Paint bgLinePaint;
    private Paint bgPaint;
    private Paint arrowPaint;
    private Thread thread;
    private Random random;

    public Manager(MainView view, int level, int kills) {
        if (MainActivity.getNewGame()) {
            timer = new Timer(0);
        }
        else {
            timer = new Timer(Store.readLong(view.getContext().getApplicationContext(), R.string.time_key, 0));
        }

        this.level = level;
        this.kills = kills;

        this.boundLeft = -10f;
        this.boundTop = -7.5f;
        this.boundRight = 10f;
        this.boundBottom = 7.5f;
        this.boundPaint = new Paint();
        this.boundPaint.setColor(0xa0ff0000);

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
            director.setCredits(30 + 30 * level);
            director.generateEnemies();
            for (int i = 0; i < enemies.size(); i++) {
                if (enemies.get(i) != null) {
                    enemies.get(i).startThread();
                }
            }
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
                // Give the player a random upgrade when finishing a level
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

                // Show the level end dialog, and wait until it's closed before continuing
                timer.setPaused(true);
                this.view.getActivity().showLevelEndDialog();
                while (this.view.getActivity().isLevelEndDialogShown()) {
                }
                this.level++;
                player.healToFull();
                healthPacks.clear();
                timer.setPaused(false);
            }
        }
        timer.setPaused(true);
        Store.saveLong(view.getActivity().getApplicationContext(), R.string.time_key, 0);
        Store.saveInt(view.getActivity().getApplicationContext(), R.string.level_key, 1);
        Store.saveInt(view.getActivity().getApplicationContext(), R.string.kills_key, 0);
        if (!this.view.getActivity().isDestroyed()) {
            this.view.getActivity().showGameOverDialog();
        }
    }

    public ArrayList<HealthPack> getHealthPacks() {
        return healthPacks;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    // Add a health pack at a random spot within the map bounds
    private void addHealthPack() {
        Vector2 pos = new Vector2(this.random.nextFloat() * (this.boundRight - this.boundLeft) + this.boundLeft,
                this.random.nextFloat() * (this.boundBottom - this.boundTop) + this.boundTop);
        this.healthPacks.add(new HealthPack(pos, this.view));
    }

    // Draw all enemies
    public void drawEnemies(Canvas canvas) {
        for (int i = 0; i < this.enemies.size(); i++) {
            this.enemies.get(i).draw(canvas);
        }
    }

    // Draw all projectiles
    public void drawProjectiles(Canvas canvas) {
        for (int i = 0; i < playerProjectiles.size(); i++) {
            playerProjectiles.get(i).draw(canvas);
        }
        for (int j = 0; j < enemyProjectiles.size(); j++) {
            enemyProjectiles.get(j).draw(canvas);
        }
    }

    // Draw all health packs
    public void drawHealthPacks(Canvas canvas) {
        for (int i = 0; i < healthPacks.size(); i++) {
            healthPacks.get(i).draw(canvas);
        }
    }

    // Draw the tiled background
    public void drawBackground(Canvas canvas) {
        float x = player.getPosition().getX(), y = player.getPosition().getY();
        float heightInUnits = this.view.getDisplaySize().getY() / this.view.getDisplaySize().getX() *
                this.view.getWidthInUnits();
        int horizontalLines = (int)Math.ceil(heightInUnits * 2 + 1);
        int verticalLines = (int)Math.ceil(this.view.getWidthInUnits() * 2 + 1);
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

    // Draw the map bounds
    public void drawBounds(Canvas canvas) {
        float x = player.getPosition().getX(), y = player.getPosition().getY();
        canvas.drawRect(0f, 0f,
                view.positionToPixels(view.relativePosition(new Vector2(boundLeft, 0f))).getX(),
                view.getDisplaySize().getY(), boundPaint);
        canvas.drawRect(view.positionToPixels(view.relativePosition(new Vector2(boundLeft, 0f))).getX(),
                0f, view.positionToPixels(view.relativePosition(new Vector2(boundRight, 0f))).getX(),
                view.positionToPixels(view.relativePosition(new Vector2(0f, boundTop))).getY(), boundPaint);
        canvas.drawRect(view.positionToPixels(view.relativePosition(new Vector2(boundRight, 0f))).getX(),
                0f, view.getDisplaySize().getX(), view.getDisplaySize().getY(), boundPaint);
        canvas.drawRect(view.positionToPixels(view.relativePosition(new Vector2(boundLeft, 0f))).getX(),
                view.positionToPixels(view.relativePosition(new Vector2(0f, boundBottom))).getY(),
                view.positionToPixels(view.relativePosition(new Vector2(boundRight, 0f))).getX(),
                view.getDisplaySize().getY(), boundPaint);
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

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public int getEnemiesLeft() {
        return this.enemies.size();
    }

    public float getBoundLeft() {
        return boundLeft;
    }

    public float getBoundTop() {
        return boundTop;
    }

    public float getBoundRight() {
        return boundRight;
    }

    public float getBoundBottom() {
        return boundBottom;
    }

    // Add a projectile sourced from the player
    public void addPlayerProjectile(Projectile projectile) {
        playerProjectiles.add(projectile);
    }

    // Add a projectile sourced from an enemy
    public void addEnemyProjectile(Projectile projectile) {
        enemyProjectiles.add(projectile);
    }

    public int directorCostSum() {
        int sum = 0;
        for (int i = 0; i < enemies.size(); i++) {
            sum += enemies.get(i).getDirectorCost();
        }
        return sum;
    }

    public String getTime() {
        return timer.toString();
    }

    public long getTimeMilis() {
        return timer.getMilis();
    }
}
