package com.graphic.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Exploder extends Enemy {
    private float velocity;

    private boolean exploding;
    private int explosionCountdown;
    private float aoeRadius;
    private Paint aoePaint;

    public Exploder(Vector2 position, Manager manager, MainView view) {
        super(position, manager, view);

        this.manager = manager;
        this.velocity = 0.5f;
        this.size = 0.3f;
        this.exploding = false;
        this.explosionCountdown = 0;
        this.aoeRadius = 0.75f * (this.size + manager.getPlayer().getSize());
        this.paint.setColor(Color.CYAN);

        this.aoePaint = new Paint();
        this.aoePaint.setColor(Color.CYAN);
        this.aoePaint.setAlpha(0);

        this.directorCost = EnemyTypeData.EXPLODER.getDirectorCost();
        this.baseDamage = EnemyTypeData.EXPLODER.getBaseDamage();
        this.baseHealth = EnemyTypeData.EXPLODER.getBaseHealth();

        this.damage = baseDamage;
        this.health = baseHealth * (0.8f + 0.2f * this.manager.getLevel());
        this.maxHealth = this.health;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(this.findPixelPosition().getX(), this.findPixelPosition().getY(),
                aoeRadius * view.getPixelsPerUnit(), aoePaint);
        super.draw(canvas);
    }

    @Override
    protected void behave() {
        if (Vector2.distance(position, manager.getPlayer().getPosition()) != 0) {
            this.positionChange = Vector2.sub(position, manager.getPlayer().getPosition());
            this.positionChange.setLength(waitTime / 1000f * velocity);
            this.position.setX(this.position.getX() + this.positionChange.getX());
            this.position.setY(this.position.getY() + this.positionChange.getY());
        }
        if (this.exploding) {
            this.explosionCountdown--;
            if (explosionCountdown > 0) {
                this.paint.setARGB(255, (int)((1 - explosionCountdown / 50f) * 255), 255, 255);
            }
            else if (this.explosionCountdown == 0) {
                if (Vector2.distance(position, manager.getPlayer().getPosition()) <
                        0.75f * (this.size + manager.getPlayer().getSize()) + 0.5f * manager.getPlayer().getSize()) {
                    manager.getPlayer().damage(this.damage);
                }
                this.alive = false;
            }
        }
        else if (Vector2.distance(position, manager.getPlayer().getPosition()) <
                    0.75f * (this.size + manager.getPlayer().getSize())) {
            this.exploding = true;
            this.explosionCountdown = 50;
            this.aoePaint.setAlpha(100);
        }
    }
}
