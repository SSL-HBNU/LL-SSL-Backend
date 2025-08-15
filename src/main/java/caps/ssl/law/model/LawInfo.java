package caps.ssl.law.model;

import caps.ssl.contract.model.ContractIssue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class LawInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "law_info_id")
    private Long id;

    private String lawName;

    private String referenceNumber;

    private String detailUrl;

    // LawInfo와 ContractIssue는 1:N 관계
    @OneToMany(mappedBy = "lawInfo", cascade = CascadeType.ALL)
    private List<ContractIssue> contractIssues = new ArrayList<>();
}