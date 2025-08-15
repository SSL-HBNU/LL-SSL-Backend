package caps.ssl.member.controller;

import caps.ssl.member.dto.MemberResDto;
import caps.ssl.member.dto.MemberSignupReqDto;
import caps.ssl.member.dto.MemberUpdateReqDto;
import caps.ssl.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> signup(@RequestBody MemberSignupReqDto requestDto) {
        Long memberId = memberService.signup(requestDto);
        return ResponseEntity.created(URI.create("/api/members/" + memberId)).build();
    }


    // 사용자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResDto> getMemberProfile(@PathVariable("id") Long memberId) {
        MemberResDto responseDto = memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(responseDto);
    }


    // 사용자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberProfile(@PathVariable("id") Long memberId, @RequestBody MemberUpdateReqDto requestDto) {
        Long updatedMemberId = memberService.updateMemberProfile(memberId, requestDto);
        return ResponseEntity.ok(updatedMemberId);
    }


    // 사용자 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}