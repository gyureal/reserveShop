package com.example.reserveshop.integrationTest;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberRepository;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.repository.ReservationRepository;
import com.example.reserveshop.reservation.web.dto.CreateReviewRequest;
import com.example.reserveshop.store.domain.entity.Store;
import com.example.reserveshop.store.domain.repository.StoreRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewIntegrationTest extends IntegrationTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void init() {
        reservation = makeReservation();
    }

    @Test
    @DisplayName("리뷰를 생성합니다.")
    void createReviewSuccess() {
        // given
        CreateReviewRequest request = CreateReviewRequest.builder()
                .reservationId(reservation.getId())
                .starRate(4.5)
                .title("맛있어요")
                .comment("정말")
                .build();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reviews")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private Reservation makeReservation() {
        Clock clock = Clock.fixed(Instant.parse("2020-03-10T12:00:00.000Z"), ZoneOffset.UTC);
        String PHONE_NUMBER = "010-0000-0000";

        return reservationRepository.save(Reservation.builder()
                .member(makeMember())
                .store(makeStore())
                .phoneNumber(PhoneNumber.of(PHONE_NUMBER))
                .reserveDateTime(LocalDateTime.now(clock))
                .build());
    }


    private Member makeMember() {
        return memberRepository.save(Member.builder()
                .id(1L)
                .build());
    }

    private Store makeStore() {
        return storeRepository.save(Store.builder()
                .id(1L)
                .build());
    }

}
