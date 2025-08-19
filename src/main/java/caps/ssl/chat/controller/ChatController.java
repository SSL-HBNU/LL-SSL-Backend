package caps.ssl.chat.controller;

import caps.ssl.chat.dto.ChatMessageDto;
import caps.ssl.chat.dto.ChatReqDto;
import caps.ssl.chat.dto.ChatResDto;
import caps.ssl.chat.dto.ChatRoomDto;
import caps.ssl.chat.model.ChatMessage;
import caps.ssl.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts/{contractId}/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms(@PathVariable Long contractId) {
        return ResponseEntity.ok(chatService.getChatRooms(contractId));
    }

    @GetMapping("/rooms/{chatRoomId}")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@PathVariable Long contractId, @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatService.getChatHistory(chatRoomId));
    }

    @PostMapping("/rooms/{chatRoomId}")
    public ResponseEntity<?> postMessageToExistingRoom(
            @PathVariable Long contractId,
            @PathVariable Long chatRoomId,
            @RequestBody ChatReqDto requestDto) {
        try {
            ChatResDto response = chatService.postMessage(contractId, chatRoomId, requestDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> postMessageToNewRoom(
            @PathVariable Long contractId,
            @RequestBody ChatReqDto requestDto) {
        try {
            ChatResDto response = chatService.postMessage(contractId, 0L, requestDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
