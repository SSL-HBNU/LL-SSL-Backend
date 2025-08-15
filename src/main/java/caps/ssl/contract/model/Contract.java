package caps.ssl.contract.model;

import caps.ssl.member.model.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String imagePath;

    @Lob
    private String ocrText;

    // analysisResult 필드는 삭제됨

    // Contract와 ContractIssue는 1:N 관계
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContractIssue> contractIssues = new ArrayList<>();
}