package com.programmers.library.domain.seat;

public class SequenceNumberGenerator {
    private static long number;

    private SequenceNumberGenerator() {}

    protected static Long getNumber() {
        return ++number;
    }

    public static void initNumber() {
        number = 0;
    }
}
