package com.programmers.library.domain.seat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

    @DisplayName("개방형, 폐쇠형 문자열로 카테고리를 찾을 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"개방형", "폐쇠형"})
    void find(String description) {
        //nothing given

        //when
        Category category = Category.find(description);

        //then
        assertThat(category.getDescription()).isEqualTo(description);
    }

    @DisplayName("카테고리를 찾을 수 없으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"wrongCategory", "예외값"})
    void throwExceptionWhenDescriptionIsWrong(String description) {
        //nothing given

        //when, then
        assertThatThrownBy(() -> Category.find(description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("해당 종류의 좌석은 없습니다. 입력값: %s", description));
    }
}
