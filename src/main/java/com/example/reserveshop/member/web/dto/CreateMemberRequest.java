package com.example.reserveshop.member.web.dto;

import com.example.reserveshop.global.constant.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@Builder
public class CreateMemberRequest {
    @NotBlank
    private final String loginId;
    @NotBlank
    private final String password;
    @NotBlank
    private final String memberName;
    @NotBlank
    private final String phoneNumber;
    @NotBlank
    private final String location;
    @NotNull
    private final MemberType memberType;
}
