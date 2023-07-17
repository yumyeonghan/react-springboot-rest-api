package com.programmers.library.domain.seat;

public class SequenceNumberGenerator {
    private static long number;

    private SequenceNumberGenerator() {}

    protected static Long getNumber() {
        return ++number;
    }
}
