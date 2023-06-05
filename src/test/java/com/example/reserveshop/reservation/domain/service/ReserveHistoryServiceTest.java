package com.example.reserveshop.reservation.domain.service;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberRepository;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.entity.ReserveHistory;
import com.example.reserveshop.reservation.domain.repository.ReservationRepository;
import com.example.reserveshop.reservation.domain.repository.ReserveHistoryRepository;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import com.example.reserveshop.store.domain.entity.Store;
import com.example.reserveshop.store.domain.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.example.reserveshop.reservation.domain.vo.ReserveStatus.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DirtiesContext
class ReserveHistoryServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReserveHistoryRepository reserveHistoryRepository;

    ReserveHistoryService reserveHistoryService;

    private Reservation reservation1;


    @BeforeEach
    private void init() {
        reservation1 = insertReservation();

        reserveHistoryService = new ReserveHistoryService(reserveHistoryRepository);
    }

    @Test
    @DisplayName("예약 히스토리를 새로 추가합니다.")
    void createNew_Success() {
        // given
        reservation1.changeStatus(REQUEST);

        // when
        ReserveHistory reserveHistory = reserveHistoryService.createBy(reservation1);

        System.out.println("------");
        System.out.println(reserveHistory);
        assertThat(reserveHistory.getStatusHistory()).isEqualTo(REQUEST);
        assertThat(reserveHistory.getReservation()).isEqualTo(reservation1);
    }

    @Test
    @DisplayName("예약으로 예약 히스토리를 조회합니다.")
    void getHistoryByReservation_Success() {
        // given
        reservation1.changeStatus(REQUEST);
        ReserveHistory reserveHistory1 = reserveHistoryService.createBy(reservation1);
        reservation1.changeStatus(APPROVED);
        ReserveHistory reserveHistory2 = reserveHistoryService.createBy(reservation1);
        reservation1.changeStatus(VISIT);
        ReserveHistory reserveHistory3 = reserveHistoryService.createBy(reservation1);

        // when
        List<ReserveHistory> result = reserveHistoryService.getHistoryByReservation(reservation1);

        List<ReserveStatus> statuses = List.of(REQUEST, APPROVED, VISIT);
        assertThat(result).hasSize(3);
        assertThat(result).extracting(ReserveHistory::getStatusHistory)
                .isEqualTo(statuses);
    }

    private Reservation insertReservation() {

        return reservationRepository.save(makeReservation());
    }

    private Reservation makeReservation() {
        Clock clock = Clock.fixed(Instant.parse("2020-03-10T12:00:00.000Z"), ZoneOffset.UTC);
        String PHONE_NUMBER = "010-0000-0000";

        return Reservation.builder()
                .member(makeMember())
                .store(makeStore())
                .phoneNumber(PhoneNumber.of(PHONE_NUMBER))
                .reserveDateTime(LocalDateTime.now(clock))
                .build();
    }


    private Member makeMember() {
        return memberRepository.save(Member.builder()
                .id(1L)
                .build());
    }

    private Store makeStore() {
        return storeRepository.save(Store.builder()
                .id(1L)
                .build());
    }

    private ReserveHistory findById(Long id) {
        return reserveHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("(테스트) 예약 정보가 없습니다."));
    }

}
