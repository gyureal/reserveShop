package com.example.reserveshop.reservation.domain.repository;

import com.example.reserveshop.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
