package caps.ssl.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatResDto {
    private Long chatRoomId;
    private String answer;
}