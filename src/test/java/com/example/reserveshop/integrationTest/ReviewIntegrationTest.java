package com.example.reserveshop.integrationTest;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberRepository;
import com.example.reserveshop.member.vo.PhoneNumber;
import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.repository.ReservationRepository;
import com.example.reserveshop.reservation.web.dto.CreateReviewRequest;
import com.example.reserveshop.reservation.web.dto.ReviewInfo;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewIntegrationTest extends IntegrationTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ReservationRepository reservationRepository;

    private Member member;
    private Store store;

    @BeforeEach
    void init() {
        member = makeMember();
        store = makeStore();

    }

    @Test
    @DisplayName("리뷰를 생성합니다.")
    void createReviewSuccess() {
        //given
        Reservation reservation = makeReservation(store);
        CreateReviewRequest request1 = CreateReviewRequest.builder()
                .reservationId(reservation.getId())
                .starRate(4.5)
                .title("맛있어요")
                .comment("정말")
                .build();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reviews")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("상점 id로 리뷰를 조회합니다.")
    void searchReviewByStoreIdSuccess() {
        // given
        Reservation reservation = makeReservation(store);
        Reservation reservation2 = makeReservation(store);
        CreateReviewRequest request1 = CreateReviewRequest.builder()
                .reservationId(reservation.getId())
                .starRate(4.5)
                .title("맛있어요")
                .comment("정말")
                .build();
        CreateReviewRequest request2 = CreateReviewRequest.builder()
                .reservationId(reservation2.getId())
                .starRate(4.5)
                .title("맛있어요")
                .comment("정말")
                .build();

       RestAssured
                .given().log().all()
                .body(request1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reviews")
                .then().log().all()
                .extract();
        RestAssured
                .given().log().all()
                .body(request2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reviews")
                .then().log().all()
                .extract();

        Long storeId = reservation.getStore().getId();
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("storeId", storeId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reviews")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<ReviewInfo> reviews = response.body().jsonPath().getList(".", ReviewInfo.class);
        assertThat(reviews).hasSize(2);
    }

    private Reservation makeReservation(Store store) {
        Clock clock = Clock.fixed(Instant.parse("2020-03-10T12:00:00.000Z"), ZoneOffset.UTC);
        String PHONE_NUMBER = "010-0000-0000";

        return reservationRepository.save(Reservation.builder()
                .member(member)
                .store(store)
                .phoneNumber(PhoneNumber.of(PHONE_NUMBER))
                .reserveDateTime(LocalDateTime.now(clock))
                .build());
    }


    private Member makeMember() {
        return memberRepository.save(Member.builder()
                .build());
    }

    private Store makeStore() {
        return storeRepository.save(Store.builder()
                .build());
    }

}
