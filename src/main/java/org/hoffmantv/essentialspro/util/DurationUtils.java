package org.hoffmantv.essentialspro.util;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Duration parsing/formatting helpers for command durations.
 */
public final class DurationUtils {

    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)([smhd])");

    private DurationUtils() {
    }

    public static long parseDurationSeconds(String durationString, long defaultSeconds) {
        if (durationString == null) {
            return defaultSeconds;
        }
        Matcher matcher = DURATION_PATTERN.matcher(durationString);
        if (!matcher.matches()) {
            return defaultSeconds;
        }
        long duration = Long.parseLong(matcher.group(1));
        switch (matcher.group(2)) {
            case "s":
                return duration;
            case "m":
                return TimeUnit.MINUTES.toSeconds(duration);
            case "h":
                return TimeUnit.HOURS.toSeconds(duration);
            case "d":
                return TimeUnit.DAYS.toSeconds(duration);
            default:
                return defaultSeconds;
        }
    }

    public static String formatDuration(long durationInSeconds) {
        long days = durationInSeconds / 86400;
        long hours = (durationInSeconds % 86400) / 3600;
        long minutes = (durationInSeconds % 3600) / 60;
        long seconds = durationInSeconds % 60;
        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}
