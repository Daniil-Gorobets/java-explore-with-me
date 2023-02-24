package ru.practicum.ewm.util.time.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeConverter {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final LocalDateTime MAX_TIME = toTime("5000-01-01 00:00:00");
    public static final LocalDateTime MIN_TIME = toTime("2000-01-01 00:00:00");


    public static String toString(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER);
    }

    public static LocalDateTime toTime(String timeString) throws DateTimeParseException {
        return LocalDateTime.parse(timeString, FORMATTER);
    }
}
