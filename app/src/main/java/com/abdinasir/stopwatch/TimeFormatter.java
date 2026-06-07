package com.abdinasir.stopwatch;

import java.util.Locale;

/**
 * TimeFormatter.java
 *
 * Converts elapsed milliseconds to the display string:
 *   HH:MM:SS.CS
 *   e.g. 00:00:05.42
 *
 * Author: Abdinasir Osman Warsame
 * Internship: Oasis Infobyte Android Application Development Internship
 * Task 5: Stop Watch
 */
public class TimeFormatter {

    private TimeFormatter() {
        // Utility class – no instances
    }

    /**
     * @param totalMillis elapsed time in milliseconds (non-negative)
     * @return formatted string "HH:MM:SS.CS"
     */
    public static String format(long totalMillis) {
        if (totalMillis < 0) totalMillis = 0;

        long cs      = (totalMillis / 10)  % 100;  // centiseconds (0-99)
        long seconds = (totalMillis / 1000) % 60;
        long minutes = (totalMillis / 60000) % 60;
        long hours   = (totalMillis / 3600000);

        return String.format(Locale.US, "%02d:%02d:%02d.%02d",
                hours, minutes, seconds, cs);
    }
}
