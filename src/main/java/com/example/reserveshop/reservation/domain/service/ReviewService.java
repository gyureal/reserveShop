package com.example.reserveshop.reservation.domain.service;

import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.entity.Review;
import com.example.reserveshop.reservation.domain.repository.ReviewRepository;
import com.example.reserveshop.reservation.domain.vo.StarRate;
import com.example.reserveshop.reservation.web.dto.CreateReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationService reservationService;
    private final Clock clock;

    public Review createReview(CreateReviewRequest request) {
        Reservation reservation = reservationService.getReservationById(request.getReservationId());

        return reviewRepository.save(
                Review.builder()
                        .reservation(reservation)
                        .starRate(StarRate.of(request.getStarRate()))
                        .title(request.getTitle())
                        .comment(request.getComment())
                        .reviewDateTime(LocalDateTime.now(clock))
                        .build());
    }
}
