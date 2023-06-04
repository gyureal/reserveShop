package com.example.reserveshop.reservation.domain.service;

import com.example.reserveshop.member.domain.MemberService;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.repository.ReservationRepository;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import com.example.reserveshop.reservation.web.dto.CreateReservationRequest;
import com.example.reserveshop.store.domain.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final String RESERVATION_NOT_FOUND = "예약정보를 찾을 수 없습니다.";
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final StoreService storeService;
    private final Clock clock;
    public Reservation createReservation(CreateReservationRequest request) {

        return reservationRepository.save(Reservation.builder()
                        .member(memberService.getMember(request.getMemberId()))
                        .store(storeService.getStoreById(request.getStoreId()))
                        .phoneNumber(PhoneNumber.of(request.getPhoneNumber()))
                        .status(ReserveStatus.REQUEST)
                        .reserveDateTime(LocalDateTime.now(clock))
                        .build());
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(RESERVATION_NOT_FOUND));
    }
}
