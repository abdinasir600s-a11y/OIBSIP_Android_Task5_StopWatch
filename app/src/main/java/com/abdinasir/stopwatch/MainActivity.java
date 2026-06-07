package com.abdinasir.stopwatch;

import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

/**
 * MainActivity.java
 *
 * Handles UI binding and stopwatch controls.
 * Uses Handler + Runnable to update the timer display every 10ms (centisecond precision).
 * Delegates all timing logic to StopwatchTimer.
 *
 * Author: Abdinasir Osman Warsame
 * Internship: Oasis Infobyte Android Application Development Internship
 * Task 5: Stop Watch
 */
public class MainActivity extends AppCompatActivity {

    // UI components
    private TextView tvTime;
    private Button btnStart;
    private Button btnHold;
    private Button btnStop;

    // Timer logic helper
    private StopwatchTimer stopwatchTimer;

    // Handler to post UI updates on the main thread
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Whether the UI update loop is currently running (prevents duplicate loops)
    private boolean isUpdating = false;

    // Runnable that refreshes the display every ~10ms
    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (isUpdating) {
                tvTime.setText(TimeFormatter.format(stopwatchTimer.getElapsedMillis()));
                handler.postDelayed(this, 10);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Opt out of forced edge-to-edge on API 35+ to prevent black screen
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_main);

        // Bind views
        tvTime  = findViewById(R.id.tv_time);
        btnStart = findViewById(R.id.btn_start);
        btnHold  = findViewById(R.id.btn_hold);
        btnStop  = findViewById(R.id.btn_stop);

        // Create timer
        stopwatchTimer = new StopwatchTimer();

        // ----- START -----
        btnStart.setOnClickListener(v -> {
            if (!stopwatchTimer.isRunning()) {
                stopwatchTimer.start();
                startUpdating();
            }
            // If already running, pressing START again does nothing
        });

        // ----- HOLD / RESUME -----
        btnHold.setOnClickListener(v -> {
            if (stopwatchTimer.isRunning()) {
                // Pause
                stopwatchTimer.pause();
                stopUpdating();
                btnHold.setText(R.string.btn_resume);
            } else if (stopwatchTimer.isPaused()) {
                // Resume
                stopwatchTimer.resume();
                startUpdating();
                btnHold.setText(R.string.btn_hold);
            }
            // If timer was never started, HOLD does nothing
        });

        // ----- STOP -----
        btnStop.setOnClickListener(v -> {
            stopwatchTimer.stop();
            stopUpdating();
            tvTime.setText(TimeFormatter.format(0));
            btnHold.setText(R.string.btn_hold);
        });
    }

    /** Start the UI update loop (idempotent – safe to call multiple times). */
    private void startUpdating() {
        if (!isUpdating) {
            isUpdating = true;
            handler.post(updateRunnable);
        }
    }

    /** Stop the UI update loop. */
    private void stopUpdating() {
        isUpdating = false;
        handler.removeCallbacks(updateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent memory leaks – remove all pending callbacks
        handler.removeCallbacks(updateRunnable);
    }
}
