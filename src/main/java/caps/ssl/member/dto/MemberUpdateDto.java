package caps.ssl.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateDto {
    private String nickname;
    private String password;
}
