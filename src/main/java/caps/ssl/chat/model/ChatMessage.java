package caps.ssl.chat.model;

import caps.ssl.contract.model.Contract;
import caps.ssl.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Lob
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ChatMessage(ChatRoom chatRoom, Member member, SenderType senderType, String content, LocalDateTime createdAt) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.senderType = senderType;
        this.content = content;
        this.createdAt = createdAt;
    }
}