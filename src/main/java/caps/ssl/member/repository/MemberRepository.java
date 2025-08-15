package caps.ssl.member.repository;

import caps.ssl.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
