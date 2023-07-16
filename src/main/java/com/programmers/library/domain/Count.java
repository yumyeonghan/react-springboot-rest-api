package com.programmers.library.domain;

public class Count {
    private static long number;

    private Count() {}

    public static Long getNumber() {
        return ++number;
    }
}
