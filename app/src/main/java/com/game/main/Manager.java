package com.game.main;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * The object that is responsible for running the main thread of the game world.
 */
public class Manager implements Runnable {
    /** The current level. */
    private int level;
    /** The current amount of kills the player has in the current game. */
    private int kills;
    /** The current player score. */
    private int score;

    // Map bounds parameters
    /** The left bound of the map. */
    private float boundLeft;
    /** The upper bound of the map. */
    private float boundTop;
    /** The right bound of the map */
    private float boundRight;
    /** The bottom bound of the map.  */
    private float boundBottom;
    /** The paint of the map bounds. */
    private Paint boundPaint;

    /** The player object. */
    private Player player;
    /** A list of the enemies currently alive. */
    private ArrayList<Enemy> enemies;
    /** A list of all player projectiles currently on the map. */
    private ArrayList<Projectile> playerProjectiles;
    /** A list of all enemy projectiles currently on the map. */
    private ArrayList<Projectile> enemyProjectiles;
    /** A list of all health pack currently on the map. */
    private ArrayList<HealthPack> healthPacks;
    /** The director object that generates the enemies. */
    private Director director;
    /** The timer object. */
    private Timer timer;
    /** The main view of the game. */
    private MainView view;
    /** The paint of the lines in the background. */
    private Paint bgLinePaint;
    /** The paint of the background. */
    private Paint bgPaint;
    /** The paint of the arrows. */
    private Paint arrowPaint;
    /** The thread of the Manager object. */
    private Thread thread;
    /** A Random object used for generating random values. */
    private Random random;

    /**
     * Constructor for the manager object.
     * <p>
     * @param view  The view to associate with the manager.
     * @param level The initial level obtained from the view.
     * @param kills The initial kills obtained from the view.
     * @param score The initial score obtained from the view.
     */
    public Manager(MainView view, int level, int kills, int score) {
        if (MainActivity.getNewGame()) {
            timer = new Timer(0);
        }
        else {
            timer = new Timer(Store.readLong(view.getContext().getApplicationContext(), R.string.time_key, 0));
        }

        this.level = level;
        this.kills = kills;
        this.score = score;

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

    /**
     * The code executed by the thread.
     */
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
                        score += enemies.get(i).getScorePoints();
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
        Store.saveFloat(view.getActivity().getApplicationContext(), R.string.damage_key, 1f);
        Store.saveFloat(view.getActivity().getApplicationContext(), R.string.health_key, 1f);
        Store.saveFloat(view.getActivity().getApplicationContext(), R.string.attack_speed_key, 1f);
        if (!this.view.getActivity().isDestroyed()) {
            this.view.getActivity().showGameOverDialog();
        }
    }

    /**
     * Adds an enemy to the enemy list.
     * <p>
     * @param enemy The enemy to add the the list.
     */
    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    /**
     * Add a health pack at a random spot within the map bounds.
     */
    private void addHealthPack() {
        Vector2 pos = new Vector2(this.random.nextFloat() * (this.boundRight - this.boundLeft) + this.boundLeft,
                this.random.nextFloat() * (this.boundBottom - this.boundTop) + this.boundTop);
        this.healthPacks.add(new HealthPack(pos, this.view));
    }

    /**
     * Draw all enemies.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    public void drawEnemies(Canvas canvas) {
        for (int i = 0; i < this.enemies.size(); i++) {
            this.enemies.get(i).draw(canvas);
        }
    }

    /**
     * Draw all projectiles.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    public void drawProjectiles(Canvas canvas) {
        for (int i = 0; i < playerProjectiles.size(); i++) {
            playerProjectiles.get(i).draw(canvas);
        }
        for (int j = 0; j < enemyProjectiles.size(); j++) {
            enemyProjectiles.get(j).draw(canvas);
        }
    }

    /**
     * Draw all health packs.
     * <p>
     * @param canvas The canvas used for drawing.
     */
    public void drawHealthPacks(Canvas canvas) {
        for (int i = 0; i < healthPacks.size(); i++) {
            healthPacks.get(i).draw(canvas);
        }
    }

    /**
     * Draw the tiled background.
     * <p>
     * @param canvas The canvas used for drawing.
     */
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

    /**
     * Draw the map bounds.
     * <p>
     * @param canvas The canvas used for drawing.
     */
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

    /**
     * Get the current level.
     * <p>
     * @return The current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the current amount of kills.
     * <p>
     * @return The current kills.
     */
    public int getKills() {
        return kills;
    }

    /**
     * Get the current score.
     * <p>
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the player object.
     * <p>
     * @return The player object.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the main view of the game.
     * <p>
     * @return The main game view.
     */
    public MainView getView() {
        return view;
    }

    /**
     * Get the list of all enemies.
     * <p>
     * @return The list of all enemies.
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Get the left bound of the map.
     * <p>
     * @return The left map bound.
     */
    public float getBoundLeft() {
        return boundLeft;
    }

    /**
     * Get the upper bound of the map.
     * <p>
     * @return The upper map bound.
     */
    public float getBoundTop() {
        return boundTop;
    }

    /**
     * Get the right bound of the map.
     * <p>
     * @return The right map bound.
     */
    public float getBoundRight() {
        return boundRight;
    }

    /**
     * Get the bottom bound of the map.
     * <p>
     * @return The bottom map bound.
     */
    public float getBoundBottom() {
        return boundBottom;
    }

    /**
     * Add a projectile to the player projectile list.
     * <p>
     * @param projectile The projectile to add.
     */
    public void addPlayerProjectile(Projectile projectile) {
        playerProjectiles.add(projectile);
    }

    /**
     * Add a projectile to the enemy projectile list.
     * <p>
     * @param projectile The projectile to add.
     */
    public void addEnemyProjectile(Projectile projectile) {
        enemyProjectiles.add(projectile);
    }

    /**
     *
     * @return The current time of the timer in string format.
     */
    public String getTime() {
        return timer.toString();
    }

    /**
     *
     * @return The current time of the timer in milliseconds.
     */
    public long getTimeMillis() {
        return timer.getMillis();
    }
}
