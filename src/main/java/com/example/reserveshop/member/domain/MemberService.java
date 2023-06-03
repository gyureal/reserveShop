package com.example.reserveshop.member.domain;

import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.vo.Address;
import com.example.reserveshop.member.vo.LoginId;
import com.example.reserveshop.member.vo.Password;
import com.example.reserveshop.member.vo.PhoneNumber;
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
                .password(Password.of(request.getPassword()))
                .name(request.getMemberName())
                .phoneNumber(PhoneNumber.of(request.getPhoneNumber()))
                .address(Address.of(request.getLocation()))
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
