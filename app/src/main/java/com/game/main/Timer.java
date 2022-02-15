package com.game.main;

/**
 * A timer that can be paused, and is used to measure playtime.
 */
public class Timer implements Runnable {
    /** The delay between timer updates (in milliseconds). */
    private static final long DELAY = 20;

    /** The total time measured in milliseconds. */
    private long millis;
    /** True if the timer is paused, otherwise false. */
    private boolean paused;
    /** The thread of the timer. */
    private Thread thread;

    /**
     * Constructs a timer object.
     * <p>
     * @param millis The initial time in milliseconds.
     */
    public Timer(long millis) {
        this.millis = millis;
        paused = false;

        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Code Executed by the thread.
     */
    @Override
    public void run() {
        while (true) {
            while (paused) {
            }
            millis += DELAY;
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set the pause state of the timer.
     * <p>
     * @param paused The new state.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Get the time in milliseconds.
     * <p>
     * @return The time measured in milliseconds.
     */
    public long getMillis() {
        return millis;
    }

    /**
     * Get a string of the time in a MM:SS format.
     * <p>
     * @return A string representing the time.
     */
    @Override
    public String toString() {
        long sec = (millis / 1000) % 60;
        long min = millis / 60000;
        if (sec < 10) {
            return min + ":0" + sec;
        }
        return min + ":" + sec;
    }
}
