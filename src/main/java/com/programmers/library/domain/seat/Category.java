package com.programmers.library.domain.seat;

import java.util.Arrays;

public enum Category {
    OPEN("개방형"),
    CLOSED("폐쇠형"),
    ;

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public static Category find(String description) {
        return Arrays.stream(Category.values())
                .filter(category -> category.description.equals(description))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당 종류의 좌석은 없습니다. 입력값: %s", description)));
    }

    public String getDescription() {
        return description;
    }
}
