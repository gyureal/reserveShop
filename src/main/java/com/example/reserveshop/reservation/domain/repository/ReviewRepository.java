package com.example.reserveshop.reservation.domain.repository;

import com.example.reserveshop.reservation.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countByReservationId(Long reservationId);
}
