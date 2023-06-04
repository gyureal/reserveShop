package com.example.reserveshop.reservation.domain.vo;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class SearchDateRange {
    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.parse("1900-01-01");
    private static final LocalDate DEFAULT_TO_DATE = LocalDate.parse("3000-01-01");
    private static final String TO_DATE_MUST_AFTER_FROM_DATE = "검색 날짜 종료일은 시작일보다 빠를 수 없습니다.";

    private final LocalDate from;
    private final LocalDate to;

    private SearchDateRange(LocalDate from, LocalDate to) {
        validateDateRange(from, to);
        this.from = from;
        this.to = to;
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException(TO_DATE_MUST_AFTER_FROM_DATE);
        }
    }

    public LocalDateTime getFromDateTime() {
        return from.atStartOfDay();
    }

    public LocalDateTime getToDateTime() {
        return to.plusDays(1).atStartOfDay();
    }

    /**
     * Optional 값을 파라메터로 받아서 SearchDateRange 를 생성한다.
     * @param fromInput
     * @param toInput
     * @return
     */
    public static SearchDateRange of(Optional<LocalDate> fromInput, Optional<LocalDate> toInput) {
        LocalDate fromDate = DEFAULT_FROM_DATE;
        LocalDate toDate = DEFAULT_TO_DATE;
        if (fromInput.isPresent()) {
            fromDate = fromInput.get();
        }
        if (toInput.isPresent()) {
            toDate = toInput.get();
        }
        return new SearchDateRange(fromDate, toDate);
    }

}
