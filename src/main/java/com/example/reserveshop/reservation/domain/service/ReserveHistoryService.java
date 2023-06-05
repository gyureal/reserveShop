package com.example.reserveshop.reservation.domain.service;

import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.entity.ReserveHistory;
import com.example.reserveshop.reservation.domain.repository.ReserveHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveHistoryService {

    private final ReserveHistoryRepository reserveHistoryRepository;

    /**
     * 예약으로 예약 히스토리를 생성합니다.
     *
     * @param reservation
     * @return
     */
    public ReserveHistory createBy(Reservation reservation) {
        return reserveHistoryRepository.save(
                ReserveHistory.builder()
                        .reservation(reservation)
                        .statusHistory(reservation.getStatus())
                        .build());
    }

    /**
     * 예약에 해당하는 예약 히스토리 목록을 조회합니다.
     *
     * @param reservation
     * @return
     */
    public List<ReserveHistory> getHistoryByReservation(Reservation reservation) {
        return reserveHistoryRepository.findByReservation_Id(reservation.getId());
    }
}
