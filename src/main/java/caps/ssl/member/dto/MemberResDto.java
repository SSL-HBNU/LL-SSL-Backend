package caps.ssl.member.dto;


import caps.ssl.member.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResDto {
    private Long memberId;
    private String nickname;
    private String phoneNumber;

    public MemberResDto(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.phoneNumber = member.getPhoneNumber();
    }
}
