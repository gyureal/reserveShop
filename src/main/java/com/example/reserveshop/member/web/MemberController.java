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
    public ResponseEntity<?> createMember(
            @RequestBody @Valid CreateMemberRequest createMemberRequest) {
        return ResponseEntity.created(URI.create(""))
                .body(memberService.joinMember(createMemberRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfo> readMembers(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMember(id));
    }
}
