package com.example.reserveshop.integrationTest;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberRepository;
import com.example.reserveshop.member.vo.*;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import com.example.reserveshop.reservation.domain.entity.Reservation;
import com.example.reserveshop.reservation.domain.repository.ReservationRepository;
import com.example.reserveshop.reservation.domain.vo.ReserveStatus;
import com.example.reserveshop.reservation.web.dto.CreateReservationRequest;
import com.example.reserveshop.reservation.web.dto.ReservationInfo;
import com.example.reserveshop.store.domain.entity.Store;
import com.example.reserveshop.store.domain.repository.StoreRepository;
import com.example.reserveshop.store.domain.vo.Image;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static com.example.reserveshop.reservation.domain.vo.ReserveStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ReservationIntegrationTest extends IntegrationTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2020-03-10T12:00:00.000Z"), ZoneOffset.UTC);
    private static final String PHONE_NUMBER = "010-0000-0000";

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @SpyBean
    private Clock clock;

    private Member member1;
    private Store store1;
    private Store store2;
    CreateReservationRequest request;

    @BeforeEach
    void init() {
        // member
        member1 = memberRepository.save(Member.builder()
                .id(1L)
                .loginId(LoginId.of("loge11"))
                .password(Password.of("00201"))
                .name("김진주")
                .phoneNumber(PhoneNumber.of("010-2233-2221"))
                .address(Address.of("2211fsd"))
                .memberType(MemberType.PARTNER)
                .build());

        // store
        store1 = storeRepository.save(Store.builder()
                .name("맥도날드 삼산점")
                .admin(member1)
                .address(Address.of("address1"))
                .description("desc1")
                .image(Image.of("/image1"))
                .phoneNumber(PhoneNumber.of("010-1111-1111"))
                .build());

        store2 = storeRepository.save(Store.builder()
                .name("맥도날드 강남점")
                .admin(member1)
                .address(Address.of("address2"))
                .description("desc2")
                .image(Image.of("/image2"))
                .phoneNumber(PhoneNumber.of("010-2222-2222"))
                .build());

        // request
        request = CreateReservationRequest.builder()
                .storeId(2L)
                .memberId(1L)
                .phoneNumber("010-1111-1111")
                .build();
    }

    @Test
    @DisplayName("예약을 생성합니다.")
    void createReservationSuccess() {
        // given
        CreateReservationRequest request = CreateReservationRequest.builder()
                .storeId(store1.getId())
                .memberId(member1.getId())
                .phoneNumber(PHONE_NUMBER)
                .build();

        doReturn(Instant.now(FIXED_CLOCK))
                .when(clock).instant();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);

        ReservationInfo result = response.body().as(ReservationInfo.class);
        LocalDateTime test = LocalDateTime.now(FIXED_CLOCK);
        ReservationInfo answer = ReservationInfo.fromEntity(makeReservation(member1, store1, REQUEST, LocalDateTime.now(FIXED_CLOCK)));
        assertThat(result).usingRecursiveComparison().ignoringFields("id", "reserveDateTime").isEqualTo(answer);
    }

    @Test
    @DisplayName("예약을 ID로 조회합니다.")
    void searchReservationByIdSuccess() {
        // given
        ReservationInfo createdInfo = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract().as(ReservationInfo.class);

        Long id = createdInfo.getId();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservations/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body().as(ReservationInfo.class)).usingRecursiveComparison()
                .isEqualTo(createdInfo);
    }

    @Test
    @DisplayName("특정 상점의 예약을 날짜 범위로 조회합니다.")
    void searchReservationsByStoreIdAndDateRange() {
        // given
        ReservationInfo reservation1 = insertReservation(member1, store1, REQUEST, LocalDateTime.parse("2020-03-10T12:00:00"));
        ReservationInfo reservation2 = insertReservation(member1, store1, REQUEST, LocalDateTime.parse("2020-03-12T13:00:00"));
        ReservationInfo reservation3 = insertReservation(member1, store1, REQUEST, LocalDateTime.parse("2020-03-14T14:00:00"));

        Long storeId = store1.getId();
        ReserveStatus status = REQUEST;
        String from = "2020-03-10";
        String to = "2020-03-12";

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("storeId", storeId)
                .queryParam("status", status)
                .queryParam("from", from)
                .queryParam("to", to)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);

        List<ReservationInfo> result = response.body().jsonPath().getList(".", ReservationInfo.class);
        List<ReservationInfo> answer = List.of(reservation1, reservation2);
        assertThat(result).usingRecursiveComparison().isEqualTo(answer);
    }

    @Test
    @DisplayName("예약을 승인 처리합니다.")
    void approveReservationSuccess() {
        // given
        ReservationInfo reservationInfo = insertReservation(member1, store1, REQUEST, LocalDateTime.now(FIXED_CLOCK));

        Long id = reservationInfo.getId();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations/{id}/approve")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id를 찾을 수 없습니다."));
        assertThat(reservation.getStatus()).isEqualTo(APPROVED);
    }

    @Test
    @DisplayName("예약을 거절 처리합니다.")
    void rejectReservationSuccess() {
        // given
        ReservationInfo reservationInfo = insertReservation(member1, store1, REQUEST, LocalDateTime.now(FIXED_CLOCK));

        Long id = reservationInfo.getId();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations/{id}/reject")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id를 찾을 수 없습니다."));
        assertThat(reservation.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    @DisplayName("예약을 방문 처리합니다.")
    void visitReservationSuccess() {
        // given
        ReservationInfo reservationInfo = insertReservation(member1, store1, APPROVED, LocalDateTime.now(FIXED_CLOCK));

        Long id = reservationInfo.getId();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations/{id}/visit")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id를 찾을 수 없습니다."));
        assertThat(reservation.getStatus()).isEqualTo(VISIT);
    }

    private ReservationInfo insertReservation(Member member, Store store, ReserveStatus status, LocalDateTime dateTime) {
        return ReservationInfo.fromEntity(reservationRepository.save(makeReservation(member, store, status, dateTime)));
    }

    private Reservation makeReservation(Member member, Store store, ReserveStatus status, LocalDateTime dateTime) {
        return Reservation.builder()
                .member(member)
                .store(store)
                .phoneNumber(PhoneNumber.of(PHONE_NUMBER))
                .status(status)
                .reserveDateTime(dateTime)
                .build();
    }
}
