package com.example.reserveshop.reservation.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class StarRateTest {

    @Test
    @DisplayName("별점 범위에 벗어날 경우 예외를 던집니다.")
    void starRateOutOfRange() {
        Double rate = 5.1;

        assertThatThrownBy(() -> {
            new StarRate(rate);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("별점을 소수점 첫째자리 까지만 저장합니다.")
    void starRateFloor() {
        Double rate = 3.353;

        StarRate starRate = new StarRate(rate);

        assertThat(starRate.getValue()).isEqualTo(3.3);
    }
}
