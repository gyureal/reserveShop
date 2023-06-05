package com.example.reserveshop.reservation.domain.service;

import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.entity.Review;
import com.example.reserveshop.reservation.domain.repository.ReviewRepository;
import com.example.reserveshop.reservation.domain.vo.StarRate;
import com.example.reserveshop.reservation.web.dto.CreateReviewRequest;
import com.example.reserveshop.reservation.web.dto.ReviewInfo;
import com.example.reserveshop.store.domain.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private static final String ALREADY_REVIEW_EXISTS = "이미 해당 예약건에 대한 리뷰가 존재합니다.";
    private static final String IS_NOT_REVIEWABLE_STATUS = "예약이 리뷰를 남길 수 있는 상태가 아닙니다.";
    private final StoreService storeService;
    private final ReviewRepository reviewRepository;
    private final ReservationService reservationService;
    private final Clock clock;

    /**
     * 리뷰를 생성합니다.
     * @throws IllegalArgumentException
     *         예약 Id 에 해당하는 예약이 존재하지 않을 때,
     *         별점이 범위를 벗어날 때,
     *         즉,{@code starRate < 0 || starRate > 5}인 경우
     * @throws IllegalStateException
     *         이미 해당 예약에 대한 리뷰가 존재하는 경우,
     *         예약이 리뷰를 남길 수 없는 상태인 경우 (VISIT, PAID 이외 상태)
     * @param request
     * @return
     */
    public Review createReview(CreateReviewRequest request) {
        Reservation reservation = reservationService.getReservationById(request.getReservationId());

        if (!reservation.idReviewable()) {
            throw new IllegalStateException(IS_NOT_REVIEWABLE_STATUS);
        }

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

    /**
     * 상점 id에 해당하는 상점 정보를 조회합니다.
     * @param storeId
     * @return
     */
    public List<ReviewInfo> getReviewsByStoreId(Long storeId) {
        storeService.getStoreById(storeId);
        return reviewRepository.findByReservation_Store_Id(storeId).stream()
                .map(ReviewInfo::fromEntity)
                .collect(Collectors.toList());
    }
}
