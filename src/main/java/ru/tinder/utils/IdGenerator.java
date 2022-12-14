package ru.tinder.utils;

public class IdGenerator {

    private static long uniqueId = 0L;

    public static synchronized long getUniqueId() {
        return uniqueId++;
    }

}
