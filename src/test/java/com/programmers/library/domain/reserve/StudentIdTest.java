package com.programmers.library.domain.reserve;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentIdTest {

    @DisplayName("학번이 비어있으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwExceptionWhenStudentIdIsNullOrEmpty(String studentId) {
        //nothing given

        //when, then
        assertThatThrownBy(() -> new StudentId(studentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("학번이 비어있습니다.");
    }

    @DisplayName("학번이 9자리가 아니거나, 숫자가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "number", "1234567"})
    void throwExceptionWhenStudentIdIsOutOfFormat(String studentId) {
        //nothing given

        //when, then
        assertThatThrownBy(() -> new StudentId(studentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("학번을 다시 입력해 주세요.");
    }
}
