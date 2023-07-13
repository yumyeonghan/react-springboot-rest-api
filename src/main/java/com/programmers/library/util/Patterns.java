package com.programmers.library.util;

import java.util.regex.Pattern;

public class Patterns {
    public static final Pattern STUDENT_ID_PATTERN = Pattern.compile("\\d{9}");

    private Patterns() {
    }
}
