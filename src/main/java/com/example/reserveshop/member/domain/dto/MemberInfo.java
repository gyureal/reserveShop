package com.example.reserveshop.member.domain.dto;

import com.example.reserveshop.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MemberInfo {
    private Long id;
    private String loginId;
    private String memberName;
    private String phoneNumber;
    private String location;
    private String memberType;

    public static MemberInfo fromEntity(Member member) {
        return MemberInfo.builder()
                .id(member.getId())
                .loginId(member.getLoginId().getValue())
                .memberName(member.getName())
                .phoneNumber(member.getPhoneNumber().getValue())
                .location(member.getAddress().getValue())
                .memberType(member.getMemberType().name())
                .build();
    }
}
