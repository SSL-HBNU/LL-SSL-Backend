package caps.ssl.member.service;

import caps.ssl.member.dto.*;
import caps.ssl.member.model.Member;
import caps.ssl.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long signup(MemberSignupReqDto requestDto) {
        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        if (memberRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .password(requestDto.getPassword())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }


    // 사용자 상세 조회
    @Transactional(readOnly = true)
    public MemberResDto getMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id=" + memberId));

        return new MemberResDto(member);
    }

    // 사용자 정보 수정
    @Transactional
    public Long updateMemberProfile(Long memberId, MemberUpdateReqDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id=" + memberId));

        member.update(requestDto.getNickname(), requestDto.getPassword());
        return memberId;
    }

    // 사용자 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id=" + memberId));
        memberRepository.delete(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id: " + id));
    }

    // 로그인 기능
    @Transactional(readOnly = true)
    public LoginResDto login(LoginReqDto requestDto) {
        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 닉네임입니다."));

        if (!member.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResDto(member.getId());
    }
}
