package caps.ssl.contract.controller;

import caps.ssl.contract.dto.ContractResponseDTO;
import caps.ssl.contract.model.Contract;
import caps.ssl.contract.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contract")
public class ContractController {
    private final ContractService contractService;

    @Operation(summary = "계약서 OCR")
    @PostMapping(value = "/{memberId}/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadAndAnalyzeContract(@PathVariable Long memberId, @RequestPart("file") MultipartFile file) throws Exception{
        try{
            Contract saved = contractService.save(memberId, file);
            Map<String, Object> result = contractService.analyze(saved, file);
            return ResponseEntity.ok(result);
        } catch(Exception e){
            return ResponseEntity.status(500).body("분석 오류: " + e.getMessage());
        }
    }

    @Operation(summary = "계약서 목록 조회")
    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> getAllContracts(@RequestParam Long memberId) {
        List<Contract> contracts = contractService.findAllByMemberId(memberId);
        List<ContractResponseDTO> result = contracts.stream().map(ContractResponseDTO::new).toList();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "계약서 상세 조회")
    @GetMapping(value = "/{contractId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContractResponseDTO> getContractById(
            @PathVariable Long contractId
    ) {
        Contract contract = contractService.findById(contractId);
        return ResponseEntity.ok(new ContractResponseDTO(contract));
    }


}
