package br.com.kio.gridsync.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    public static boolean isPeakHour(LocalDateTime dateTime) {          // CALCULA HORARIO DE PICO
        int hour = dateTime.getHour();
        return (hour >= 17 && hour < 21) || (hour >= 7 && hour < 9);
    }
}