package com.example.reserveshop.reservation.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReserveStatus {
    REQUEST("요청"),
    APPROVED("승인"),
    REJECTED("거절"),
    VISIT("방문"),
    PAID("지불");

    private final String title;
}
