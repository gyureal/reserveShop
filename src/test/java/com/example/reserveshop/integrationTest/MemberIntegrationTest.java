package com.example.reserveshop.integrationTest;

import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.*;

public class MemberIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("회원가입에 성공합니다.")
    void memberJoinSuccess() {
        // given
        CreateMemberRequest request = CreateMemberRequest.builder().build();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("1번 id의 회원을 조회합니다.")
    void readMemberSuccess() {
        Long memberId = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/members/{id}")
                        .then().log().all()
                        .extract();
        // then
        MemberInfo answer = MemberInfo.builder().build();

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).usingRecursiveComparison().isEqualTo(answer);
    }
}
