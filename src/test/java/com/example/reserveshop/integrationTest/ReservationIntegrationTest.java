package com.example.reserveshop.integrationTest;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberRepository;
import com.example.reserveshop.member.vo.*;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import com.example.reserveshop.reservation.web.dto.CreateReservationRequest;
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
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationIntegrationTest extends IntegrationTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StoreRepository storeRepository;

    private Member member1;
    private Store store1;
    private Store store2;

    @BeforeEach
    void dataInit() {
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
    }

    @Test
    @DisplayName("예약을 생성합니다.")
    void createReservationSuccess() {
        // given
        CreateReservationRequest request = CreateReservationRequest.builder()
                .storeId(2L)
                .memberId(1L)
                .phoneNumber("010-1111-1111")
                .build();

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
    }
}
