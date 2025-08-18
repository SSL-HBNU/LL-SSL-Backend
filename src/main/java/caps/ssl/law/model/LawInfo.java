package caps.ssl.law.model;

import caps.ssl.contract.model.Contract;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class LawInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "law_info_id")
    private Long id;

    private String lawName;

    private String referenceNumber;

    private String detailUrl;

    private String lawSerialNumber;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    @JsonBackReference
    private Contract contract;
}