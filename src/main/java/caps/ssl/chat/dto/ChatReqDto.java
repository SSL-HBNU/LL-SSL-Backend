package caps.ssl.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatReqDto {
    private Long memberId;
    private String message;
}