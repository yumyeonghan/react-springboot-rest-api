package com.programmers.library.domain;

import com.programmers.library.util.Patterns;

import java.util.Objects;

public record StudentId(String studentId) {
    public StudentId {
        validateStudentId(studentId);
    }

    private static void validateStudentId(String studentId) {
        if (Objects.isNull(studentId) || studentId.isBlank()) {
            throw new IllegalArgumentException("학번이 비어있습니다.");
        }
        if (!Patterns.STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new IllegalArgumentException("학번을 다시 입력해 주세요.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentId studentId1 = (StudentId) o;
        return Objects.equals(studentId, studentId1.studentId);
    }
}
