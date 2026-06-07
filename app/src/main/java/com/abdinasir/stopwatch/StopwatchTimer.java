package com.abdinasir.stopwatch;

/**
 * StopwatchTimer.java
 *
 * Pure logic class – no Android UI code here.
 * Tracks elapsed time in milliseconds using System.currentTimeMillis().
 *
 * States:
 *   IDLE    – never started, or reset after STOP
 *   RUNNING – ticking
 *   PAUSED  – ticking suspended, elapsed time preserved
 *
 * Author: Abdinasir Osman Warsame
 * Internship: Oasis Infobyte Android Application Development Internship
 * Task 5: Stop Watch
 */
public class StopwatchTimer {

    // Possible timer states
    private enum State { IDLE, RUNNING, PAUSED }

    private State state = State.IDLE;

    /** Wall-clock time (ms) when the timer was last started/resumed. */
    private long startTime = 0L;

    /** Accumulated ms from previous running intervals (before the last pause). */
    private long accumulatedMs = 0L;

    // ------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------

    /**
     * Start the timer from zero.
     * Safe to call only when state is IDLE.
     */
    public void start() {
        if (state == State.IDLE) {
            startTime = System.currentTimeMillis();
            accumulatedMs = 0L;
            state = State.RUNNING;
        }
    }

    /**
     * Pause the timer, preserving elapsed time.
     * Safe to call only when state is RUNNING.
     */
    public void pause() {
        if (state == State.RUNNING) {
            accumulatedMs += System.currentTimeMillis() - startTime;
            state = State.PAUSED;
        }
    }

    /**
     * Resume the timer after a pause.
     * Safe to call only when state is PAUSED.
     */
    public void resume() {
        if (state == State.PAUSED) {
            startTime = System.currentTimeMillis();
            state = State.RUNNING;
        }
    }

    /**
     * Stop and reset the timer back to zero.
     */
    public void stop() {
        state = State.IDLE;
        startTime = 0L;
        accumulatedMs = 0L;
    }

    // ------------------------------------------------------------------
    // State queries
    // ------------------------------------------------------------------

    public boolean isRunning() {
        return state == State.RUNNING;
    }

    public boolean isPaused() {
        return state == State.PAUSED;
    }

    /**
     * Returns total elapsed milliseconds.
     * Thread-safe for reading from the main thread while the timer runs.
     */
    public long getElapsedMillis() {
        if (state == State.RUNNING) {
            return accumulatedMs + (System.currentTimeMillis() - startTime);
        }
        // IDLE or PAUSED
        return accumulatedMs;
    }
}
