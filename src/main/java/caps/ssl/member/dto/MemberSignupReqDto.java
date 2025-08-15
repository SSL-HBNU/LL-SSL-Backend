package caps.ssl.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignupReqDto {

    private String nickname;
    private String password;
    private String phoneNumber;

}
