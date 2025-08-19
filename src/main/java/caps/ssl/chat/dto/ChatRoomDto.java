package caps.ssl.chat.dto;

import caps.ssl.chat.model.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomDto {
    private Long id;
    private String title;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
    }
}