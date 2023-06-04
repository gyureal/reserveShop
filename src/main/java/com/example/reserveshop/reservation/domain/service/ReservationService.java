package com.example.reserveshop.reservation.domain.service;

import com.example.reserveshop.member.domain.MemberService;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.repository.ReservationRepository;
import com.example.reserveshop.reservation.domain.vo.AcceptType;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import com.example.reserveshop.reservation.domain.vo.SearchDateRange;
import com.example.reserveshop.reservation.web.dto.CreateReservationRequest;
import com.example.reserveshop.store.domain.entity.Store;
import com.example.reserveshop.store.domain.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.source.internal.hbm.AttributesHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.reserveshop.reservation.domain.vo.AcceptType.*;
import static com.example.reserveshop.reservation.domain.vo.ReserveStatus.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final String RESERVATION_NOT_FOUND = "예약정보를 찾을 수 없습니다.";

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final StoreService storeService;
    private final Clock clock;

    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {

        return reservationRepository.save(Reservation.builder()
                        .member(memberService.getMember(request.getMemberId()))
                        .store(storeService.getStoreById(request.getStoreId()))
                        .phoneNumber(PhoneNumber.of(request.getPhoneNumber()))
                        .status(REQUEST)
                        .reserveDateTime(LocalDateTime.now(clock))
                        .build());
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(RESERVATION_NOT_FOUND));
    }

    public List<Reservation> getReservations(Long storeId, ReserveStatus status, Optional<LocalDate> from, Optional<LocalDate> to) {
        Store store = storeService.getStoreById(storeId);

        SearchDateRange dateRange = SearchDateRange.of(from, to);

        LocalDateTime fromDateTime = dateRange.getFromDateTime();
        LocalDateTime toDateTime = dateRange.getToDateTime();

        return reservationRepository.findByStoreIdAndStatusLikeAndReserveDateTimeBetween(
                storeId, status, dateRange.getFromDateTime(), dateRange.getToDateTime());
    }

    /**
     * 예약 승인/거절 처리를 합니다.
     * @throws IllegalArgumentException 예약 상태가 요청(APPROVE)이 아닌 경우
     * @param id
     */
    @Transactional
    public void approveOrRejectReservation(Long id, AcceptType acceptType) {
        Reservation reservation = getReservationById(id);
        if (acceptType.equals(APPROVE)) {
            reservation.approve();
            return;
        }
        if (acceptType.equals(REJECT)) {
            reservation.reject();
            return;
        }
    }
}
