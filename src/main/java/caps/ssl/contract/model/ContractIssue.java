package caps.ssl.contract.model;

import caps.ssl.law.model.LawInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ContractIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    // 어떤 계약서와 연관되었는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    // 어떤 법률과 연관되었는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "law_info_id")
    private LawInfo lawInfo;

    // 어떤 계약서 내용 때문에 이슈가 발생했는지 저장
    @Lob
    private String triggerText;
}