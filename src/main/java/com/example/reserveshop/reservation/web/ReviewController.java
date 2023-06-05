package com.example.reserveshop.reservation.web;

import com.example.reserveshop.reservation.domain.entity.Review;
import com.example.reserveshop.reservation.domain.service.ReviewService;
import com.example.reserveshop.reservation.web.dto.CreateReviewRequest;
import com.example.reserveshop.reservation.web.dto.ReviewInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewInfo> createReview(@Valid @RequestBody CreateReviewRequest request) {
        Review review = reviewService.createReview(request);
        return ResponseEntity.created(URI.create("/reviews" + review.getId()))
                .body(ReviewInfo.fromEntity(review));
    }
}
