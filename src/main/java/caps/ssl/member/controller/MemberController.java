package caps.ssl.member.controller;

import caps.ssl.member.dto.*;
import caps.ssl.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 사용자 추가
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberSignupReqDto requestDto) {
        try {
            Long memberId = memberService.signup(requestDto);
            return ResponseEntity.created(URI.create("/api/members/" + memberId)).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // 사용자 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberProfile(@PathVariable("id") Long memberId) {
        try {
            MemberResDto responseDto = memberService.getMemberProfile(memberId);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 사용자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMemberProfile(@PathVariable("id") Long memberId, @RequestBody MemberUpdateReqDto requestDto) {
        try {
            Long updatedMemberId = memberService.updateMemberProfile(memberId, requestDto);
            return ResponseEntity.ok(updatedMemberId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 사용자 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable("id") Long memberId) {
        try {
            memberService.deleteMember(memberId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto requestDto) {
        try {
            LoginResDto responseDto = memberService.login(requestDto);
            return ResponseEntity.ok(responseDto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}