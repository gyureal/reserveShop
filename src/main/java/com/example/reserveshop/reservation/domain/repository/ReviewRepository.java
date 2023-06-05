package com.example.reserveshop.reservation.domain.repository;

import com.example.reserveshop.reservation.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countByReservationId(Long reservationId);
    List<Review> findByReservation_Store_Id(Long storeId);
}
