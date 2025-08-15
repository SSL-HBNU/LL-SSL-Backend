package caps.ssl.contract.dto;

import caps.ssl.contract.model.Contract;
import lombok.Data;

@Data
public class ContractResponseDTO {
    private Long contractId;
    private Long memberId;
    private String imagePath;

    public ContractResponseDTO(Contract contract) {
        this.contractId = contract.getId();
        this.memberId = contract.getMember().getId();
        this.imagePath = contract.getImagePath();
    }
}