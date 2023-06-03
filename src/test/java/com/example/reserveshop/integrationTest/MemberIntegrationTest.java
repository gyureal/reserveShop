package com.example.reserveshop.integrationTest;

import com.example.reserveshop.global.constant.MemberType;
import com.example.reserveshop.member.domain.Member;
import com.example.reserveshop.member.domain.MemberRepository;
import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.vo.LoginId;
import com.example.reserveshop.member.vo.Password;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.*;

public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("일반 회원이 회원가입에 성공합니다.")
    void memberJoinSuccess() {
        // given
        CreateMemberRequest request = CreateMemberRequest
                .builder()
                .loginId("test001")
                .password("pw001")
                .memberName("김기리")
                .phoneNumber("010-1234-5678")
                .location("서울특별시 강남구")
                .memberType(MemberType.GENERAL)
                .build();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("1번 id의 회원을 조회합니다.")
    void readMemberSuccess() {
        // given
        Member member = Member.builder()
                .id(1L)
                .loginId(LoginId.of("testInit001"))
                .password(Password.of("pw001"))
                .name("김장훈")
                .phoneNumber("010-2333-3333")
                .location("test")
                .memberType(MemberType.GENERAL)
                .build();
        memberRepository.save(member);

        Long memberId = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/{id}")
                .then().log().all()
                .extract();

        // then
        MemberInfo answer = MemberInfo.fromEntity(member);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body().as(MemberInfo.class)).usingRecursiveComparison().isEqualTo(answer);
    }
}
