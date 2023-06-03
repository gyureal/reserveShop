package com.example.reserveshop.integrationTest;

import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberRepository;
import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.vo.*;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import com.example.reserveshop.store.domain.dto.StoreInfo;
import com.example.reserveshop.store.domain.repository.StoreRepository;
import com.example.reserveshop.store.domain.vo.StoreType;
import com.example.reserveshop.store.web.dto.CreateStoreRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreIntegrationTest extends IntegrationTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void dataInit() {
        memberRepository.save(Member.builder()
                        .id(1L)
                        .loginId(LoginId.of("loge11"))
                        .password(Password.of("00201"))
                        .name("김진주")
                        .phoneNumber(PhoneNumber.of("010-2233-2221"))
                        .address(Address.of("2211fsd"))
                        .memberType(MemberType.PARTNER)
                        .build());
    }

    @Test
    @DisplayName("관리자 회원이 매장등록에 성공합니다.")
    void storeJoin() {
        // given
        CreateStoreRequest request = CreateStoreRequest.builder()
                .storeName("맥도날드")
                .adminMemberId(1L)
                .address("address")
                .description("description")
                .image("/image")
                .phoneNumber("010-2022-2223")
                .build();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stores")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("매장명으로 매장을 검색합니다.")
    void searchStoreByName() {
        // given
        String storeName = "";
        String sortType = "";

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("storeName", storeName)
                .queryParam("sort", sortType)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stores")
                .then().log().all()
                .extract();


        // then
        StoreInfo[] storeInfos = response.body().as(StoreInfo[].class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(storeInfos).hasSize(3);
    }

    @Test
    @DisplayName("id값으로 매장을 검색합니다.")
    void searchStoreById() {
        // given
        Long id = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stores/{id}")
                .then().log().all()
                .extract();

        // then
        StoreInfo answer = StoreInfo
                .builder()
                .build();

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body().as(StoreInfo.class)).usingRecursiveComparison().isEqualTo(answer);
    }
}
