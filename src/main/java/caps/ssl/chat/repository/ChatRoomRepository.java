package caps.ssl.chat.repository;

import caps.ssl.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByContractIdOrderByCreatedAtDesc(Long contractId);
}