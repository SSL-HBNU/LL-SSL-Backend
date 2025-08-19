package caps.ssl.member.model;

import caps.ssl.contract.model.Contract;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String phoneNumber;


    // 💡 수정된 부분: Member가 삭제되면 모든 Contract도 함께 삭제됩니다.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    public void update(String nickname, String password) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (password != null) {
            this.password = password;
        }
    }

    @Builder
    public Member(String nickname, String password, String phoneNumber) {
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}