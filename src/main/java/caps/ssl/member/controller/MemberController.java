package caps.ssl.member.controller;

import caps.ssl.member.dto.MemberResDto;
import caps.ssl.member.dto.MemberUpdateDto;
import caps.ssl.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members") // 공통된 URL 경로 설정
public class MemberController {

    private final MemberService memberService;

    // 사용자 상세 조회 (GET /api/members/{id})
    @GetMapping("/{id}")
    public ResponseEntity<MemberResDto> getMemberProfile(@PathVariable("id") Long memberId) {
        MemberResDto responseDto = memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(responseDto);
    }

    // 사용자 정보 수정 (PUT /api/members/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberProfile(@PathVariable("id") Long memberId, @RequestBody MemberUpdateDto requestDto) {
        Long updatedMemberId = memberService.updateMemberProfile(memberId, requestDto);
        return ResponseEntity.ok(updatedMemberId);
    }

    // 사용자 삭제 (DELETE /api/members/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build(); // 내용 없이 성공적인 응답 (204 No Content)
    }
}