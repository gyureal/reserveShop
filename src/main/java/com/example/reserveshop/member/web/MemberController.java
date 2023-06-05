package com.example.reserveshop.member.web;

import com.example.reserveshop.member.domain.MemberService;
import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원을 생성합니다.
     * @param createMemberRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<MemberInfo> createMember(
            @RequestBody @Valid CreateMemberRequest createMemberRequest) {
        MemberInfo memberInfo = MemberInfo.fromEntity(memberService.joinMember(createMemberRequest));
        return ResponseEntity.created(URI.create("/members/" + memberInfo.getId()))
                .body(memberInfo);
    }

    /**
     * 전체 회원 목록을 조회합니다.
     * @return
     */
    @GetMapping
    public ResponseEntity<List<MemberInfo>> searchMembers() {
        return ResponseEntity.ok(memberService.getMembers()
                .stream().map(MemberInfo::fromEntity)
                .collect(Collectors.toList()));
    }

    /**
     * id로 회원을 조회합니다.
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberInfo> searchMembersById(@PathVariable Long id) {
        return ResponseEntity.ok(MemberInfo.fromEntity(memberService.getMember(id)));
    }
}
