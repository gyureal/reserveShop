package com.example.reserveshop.reservation.domain.repository;

import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStoreIdAndStatusLikeAndReserveDateTimeBetween(Long storeId, ReserveStatus status,
                                                                          LocalDateTime from, LocalDateTime to);
}
