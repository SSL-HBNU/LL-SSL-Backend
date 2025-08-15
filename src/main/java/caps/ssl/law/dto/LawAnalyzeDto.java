package caps.ssl.law.dto;

import caps.ssl.contract.dto.Issue;
import caps.ssl.law.model.LawInfo;
import lombok.Data;

import java.util.List;

@Data
public class LawAnalyzeDto {
    private Long contractId;
    private List<Issue> issues;
    private List<LawInfoDTO> laws;

    public LawAnalyzeDto(Long contractId, List<Issue> issues, List<LawInfoDTO> laws) {
        this.contractId = contractId;
        this.issues = issues;
        this.laws = laws;
    }
}
