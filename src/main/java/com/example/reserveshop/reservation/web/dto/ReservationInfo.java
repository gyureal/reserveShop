package com.example.reserveshop.reservation.web.dto;

import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import com.example.reserveshop.store.domain.dto.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReservationInfo {

    private Long id;
    private MemberInfo member;
    private StoreInfo store;
    private String phoneNumber;
    private ReserveStatus status;
    private LocalDateTime reserveDateTime;


    public static ReservationInfo fromEntity(Reservation reservation) {
        return ReservationInfo.builder()
                .id(reservation.getId())
                .member(MemberInfo.fromEntity(reservation.getMember()))
                .store(StoreInfo.fromEntity(reservation.getStore()))
                .phoneNumber(reservation.getPhoneNumber().getValue())
                .status(reservation.getStatus())
                .reserveDateTime(reservation.getReserveDateTime())
                .build();
    }
}
