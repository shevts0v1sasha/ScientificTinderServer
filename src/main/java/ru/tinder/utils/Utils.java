package ru.tinder.utils;

import ru.tinder.model.TDate;

import java.time.LocalDateTime;

public class Utils {

    public static TDate getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();

        return new TDate(
                now.getSecond(),
                now.getMinute(),
                now.getHour(),
                now.getDayOfMonth(),
                now.getMonthValue(),
                now.getYear()
        );

    }
}
