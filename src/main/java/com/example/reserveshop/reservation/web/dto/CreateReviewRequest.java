package com.example.reserveshop.reservation.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@Builder
public class CreateReviewRequest {
    @NotNull
    private Long reservationId;
    @NotNull
    @Min(0)
    @Max(5)
    private Double starRate;
    @NotBlank
    private String title;
    private String comment;
}
