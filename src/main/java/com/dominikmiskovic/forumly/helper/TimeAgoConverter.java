package com.dominikmiskovic.forumly.util; // Use the new package

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeAgoConverter { // Make sure the class is public

    public String convert(Instant instant) {
        Instant now = Instant.now();
        Duration duration = Duration.between(instant, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            long minutes = duration.toMinutes();
            return minutes + (minutes == 1 ? " minute" : " minutes") + " ago";
        } else if (seconds < 86400) {
            long hours = duration.toHours();
            return hours + (hours == 1 ? " hour" : " hours") + " ago";
        } else if (seconds < 2592000) { // Approximately 30 days
            long days = duration.toDays();
            return days + (days == 1 ? " day" : " days") + " ago";
        } else if (seconds < 31536000) { // Approximately 365 days
            ZonedDateTime instantZoned = instant.atZone(ZoneId.systemDefault());
            ZonedDateTime nowZoned = now.atZone(ZoneId.systemDefault());
            long months = ChronoUnit.MONTHS.between(instantZoned, nowZoned);
            return months + (months == 1 ? " month" : " months") + " ago";
        } else {
            ZonedDateTime instantZoned = instant.atZone(ZoneId.systemDefault());
            ZonedDateTime nowZoned = now.atZone(ZoneId.systemDefault());
            long years = ChronoUnit.YEARS.between(instantZoned, nowZoned);
            return years + (years == 1 ? " year" : " years") + " ago";
        }
    }
}