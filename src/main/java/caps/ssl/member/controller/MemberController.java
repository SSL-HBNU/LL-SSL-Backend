package caps.ssl.member.controller;

import caps.ssl.member.dto.MemberResDto;
import caps.ssl.member.dto.MemberUpdateDto;
import caps.ssl.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/{id}")
    public ResponseEntity<MemberResDto> getMemberProfile(@PathVariable("id") Long memberId) {
        MemberResDto responseDto = memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(responseDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberProfile(@PathVariable("id") Long memberId, @RequestBody MemberUpdateDto requestDto) {
        Long updatedMemberId = memberService.updateMemberProfile(memberId, requestDto);
        return ResponseEntity.ok(updatedMemberId);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}