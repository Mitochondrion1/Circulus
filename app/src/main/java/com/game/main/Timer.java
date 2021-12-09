package com.game.main;

import androidx.annotation.NonNull;

public class Timer implements Runnable {
    private static final long DELAY = 20;

    private long milis;
    private boolean paused;
    private Thread thread;

    public Timer(long milis) {
        this.milis = milis;
        paused = false;

        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        while (true) {
            while (paused) {
            }
            milis += DELAY;
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public long getMilis() {
        return milis;
    }

    @Override
    public String toString() {
        long sec = (milis / 1000) % 60;
        long min = milis / 60000;
        if (sec < 10) {
            return min + ":0" + sec;
        }
        return min + ":" + sec;
    }
}
