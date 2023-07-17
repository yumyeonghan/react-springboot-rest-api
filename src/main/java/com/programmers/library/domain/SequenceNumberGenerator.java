package com.programmers.library.domain;

public class SequenceNumberGenerator {
    private static long number;

    private SequenceNumberGenerator() {}

    protected static Long getNumber() {
        return ++number;
    }
}
