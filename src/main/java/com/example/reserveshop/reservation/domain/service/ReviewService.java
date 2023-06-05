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
    private static final String ALREADY_REVIEW_EXISTS = "이미 해당 예약건에 대한 리뷰가 존재합니다.";
    private final ReviewRepository reviewRepository;
    private final ReservationService reservationService;
    private final Clock clock;

    /**
     * 리뷰를 생성합니다.
     * @throws IllegalArgumentException
     *         예약 Id 에 해당하는 예약이 존재하지 않을 때,
     *         별점이 범위를 벗어날 때,
     *         즉,{@code starRate < 0 || starRate > 5}인 경우
     * @throws IllegalStateException 이미 해당 예약에 대한 리뷰가 존재하는 경우
     * @param request
     * @return
     */
    public Review createReview(CreateReviewRequest request) {
        Reservation reservation = reservationService.getReservationById(request.getReservationId());

        int reviewCount = reviewRepository.countByReservationId(request.getReservationId());
        if (reviewCount > 0) {
            throw new IllegalStateException(ALREADY_REVIEW_EXISTS);
        }

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
