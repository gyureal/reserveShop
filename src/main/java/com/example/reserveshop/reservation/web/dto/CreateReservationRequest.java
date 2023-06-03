package com.example.reserveshop.reservation.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CreateReservationRequest {
    private Long storeId;
    private Long memberId;
    private String phoneNumber;
}
