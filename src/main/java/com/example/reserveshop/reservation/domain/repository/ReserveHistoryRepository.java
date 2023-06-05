package com.example.reserveshop.reservation.domain.repository;

import com.example.reserveshop.reservation.domain.entity.ReserveHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveHistoryRepository extends JpaRepository<ReserveHistory, Long> {
    List<ReserveHistory> findByReservation_Id(Long reservationId);
}
