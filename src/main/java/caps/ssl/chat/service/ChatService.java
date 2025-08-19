package caps.ssl.chat.service;

import caps.ssl.chat.dto.ChatMessageDto;
import caps.ssl.chat.dto.ChatReqDto;
import caps.ssl.chat.dto.ChatResDto;
import caps.ssl.chat.dto.ChatRoomDto;
import caps.ssl.chat.model.ChatMessage;
import caps.ssl.chat.model.ChatRoom;
import caps.ssl.chat.model.SenderType;
import caps.ssl.chat.repository.ChatMessageRepository;
import caps.ssl.chat.repository.ChatRoomRepository;
import caps.ssl.contract.client.OpenAiClient;
import caps.ssl.contract.model.Contract;
import caps.ssl.contract.repository.ContractRepository;
import caps.ssl.member.model.Member;
import caps.ssl.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final ContractRepository contractRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    @Transactional(readOnly = true)
    public List<ChatRoomDto> getChatRooms(Long contractId) {
        return chatRoomRepository.findByContractIdOrderByCreatedAtDesc(contractId)
                .stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatHistory(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId)
                .stream()
                .map(ChatMessageDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatResDto postMessage(Long contractId, Long chatRoomId, ChatReqDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약서를 찾을 수 없습니다."));

        ChatRoom chatRoom;
        if (chatRoomId == null || chatRoomId == 0) {
            chatRoom = createNewChatRoom(contract, requestDto.getMessage());
        } else {
            chatRoom = chatRoomRepository.findById(chatRoomId)
                    .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        }

        String chatHistory = getFormattedChatHistory(chatRoom.getId());
        String contractText = contract.getOcrText();

        saveMessage(chatRoom, member, SenderType.USER, requestDto.getMessage());
        String answer = openAiClient.getChatbotResponse(requestDto.getMessage(), contractText, chatHistory);
        saveMessage(chatRoom, member, SenderType.AI, answer);

        return new ChatResDto(chatRoom.getId(), answer);
    }


    private ChatRoom createNewChatRoom(Contract contract, String firstMessage) {
        String title = firstMessage.length() > 20 ? firstMessage.substring(0, 20) + "..." : firstMessage;
        ChatRoom chatRoom = ChatRoom.builder()
                .contract(contract)
                .title(title)
                .createdAt(LocalDateTime.now())
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    private String getFormattedChatHistory(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        if (messages.isEmpty()) {
            return "이전 대화가 없습니다.";
        }
        return messages.stream()
                .map(msg -> msg.getSenderType().name() + ": " + msg.getContent())
                .collect(Collectors.joining("\n"));
    }

    private void saveMessage(ChatRoom chatRoom, Member member, SenderType sender, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(member)
                .senderType(sender)
                .content(message)
                .createdAt(LocalDateTime.now())
                .build();
        chatMessageRepository.save(chatMessage);
    }
}