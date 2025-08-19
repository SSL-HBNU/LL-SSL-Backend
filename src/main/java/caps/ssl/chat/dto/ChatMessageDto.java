package caps.ssl.chat.dto;

import caps.ssl.chat.model.ChatMessage;
import caps.ssl.chat.model.SenderType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageDto {
    private Long id;
    private SenderType senderType;
    private String content;
    private LocalDateTime createdAt;
    private Long memberId;

    public ChatMessageDto(ChatMessage message) {
        this.id = message.getId();
        this.senderType = message.getSenderType();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.memberId = message.getMember().getId();
    }
}
