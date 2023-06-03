package com.example.reserveshop.member.domain;

import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.vo.LoginId;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {
    private static final String USER_NOT_FOUND = "회원이 존재하지 않습니다.";
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param request
     * @return
     */
    public Long joinMember(CreateMemberRequest request) {
        Member member = memberRepository.save(Member.builder()
                .loginId(LoginId.of(request.getLoginId()))
                .password(request.getPassword())
                .name(request.getMemberName())
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .build());
        return member.getId();
    }

    /**
     * 회원을 조회합니다
     * @param id
     * @return
     */
    public MemberInfo getMember(Long id) {
        return MemberInfo.fromEntity(memberRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND)));
    }
}
