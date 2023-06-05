package com.example.reserveshop.reservation.web;

import com.example.reserveshop.reservation.domain.entity.Review;
import com.example.reserveshop.reservation.domain.service.ReviewService;
import com.example.reserveshop.reservation.web.dto.CreateReviewRequest;
import com.example.reserveshop.reservation.web.dto.ReviewInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰를 생성합니다.
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ReviewInfo> createReview(@Valid @RequestBody CreateReviewRequest request) {
        Review review = reviewService.createReview(request);
        return ResponseEntity.created(URI.create("/reviews" + review.getId()))
                .body(ReviewInfo.fromEntity(review));
    }

    /**
     * 상점 id 에 해당하는 리뷰 정보를 조회합니다.
     * @param storeId
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ReviewInfo>> searchReviews(@RequestParam Long storeId) {
        return ResponseEntity.ok(reviewService.getReviewsByStoreId(storeId));
    }
}
