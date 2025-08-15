package caps.ssl.member.dto;


import lombok.Getter;

@Getter
public class LoginResDto {

    private Long memberId;

    public LoginResDto(Long memberId) {
        this.memberId = memberId;
    }
}
