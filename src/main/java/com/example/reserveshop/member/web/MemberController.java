package com.example.reserveshop.member.web;

import com.example.reserveshop.member.domain.MemberService;
import com.example.reserveshop.member.domain.dto.MemberInfo;
import com.example.reserveshop.member.web.dto.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberInfo> createMember(
            @RequestBody @Valid CreateMemberRequest createMemberRequest) {
        MemberInfo memberInfo = MemberInfo.fromEntity(memberService.joinMember(createMemberRequest));
        return ResponseEntity.created(URI.create("/members/" + memberInfo.getId()))
                .body(memberInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfo> readMembers(@PathVariable Long id) {
        return ResponseEntity.ok(MemberInfo.fromEntity(memberService.getMember(id)));
    }
}
