package com.example.reserveshop.reservation.domain.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StarRate {
    private static final String MUST_ZERO_TO_FIVE = "별점은 0이상 5이하여야합니다.";
    private Double value;

    private StarRate(Double value) {
        validateRate(value);
        this.value = scale(value);
    }

    public static StarRate of(Double value) {
        return new StarRate(value);
    }

    /**
     * 소수점 첫째 자리 까지만 나타냅니다.
     * @param value
     * @return
     */
    private Double scale(Double value) {
        return Math.floor(value * 10.0) / 10.0;
    }

    private void validateRate(Double value) {
        if (value < 0 || value > 5) {
            throw new IllegalArgumentException(MUST_ZERO_TO_FIVE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StarRate starRate = (StarRate) o;
        return Objects.equals(value, starRate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
