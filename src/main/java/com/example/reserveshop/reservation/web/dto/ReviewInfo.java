package com.example.reserveshop.reservation.web.dto;

import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReviewInfo {
    private Long id;
    private Long reservationId;
    private Double starRate;
    private String title;
    private String comment;
    private LocalDateTime reviewDateTime;

    public static ReviewInfo fromEntity(Review review) {
        return ReviewInfo.builder()
                .id(review.getId())
                .reservationId(review.getReservation().getId())
                .starRate(review.getStarRate().getValue())
                .title(review.getTitle())
                .comment(review.getComment())
                .reviewDateTime(review.getReviewDateTime())
                .build();
    }
}
