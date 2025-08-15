package caps.ssl.law.controller;

import caps.ssl.law.dto.LawAnalyzeDto;
import caps.ssl.law.model.LawInfo;
import caps.ssl.law.service.LawService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LawController {

    private final LawService lawService;

    @Operation(summary = "계약서 법률정보 조회")
    @GetMapping("/contracts/{contractId}/lawinfo")
    public List<LawInfo> getLawsByContract(@PathVariable Long contractId) {
        return lawService.getLawsByContractId(contractId);
    }

    @Operation(summary = "법률정보 상세 조회")
    @GetMapping("/lawinfo/{lawInfoId}")
    public LawInfo getLawDetail(@PathVariable Long lawInfoId) {
        return lawService.getLawById(lawInfoId);
    }

    @Operation(summary = "법률 정보 분석")
    @PostMapping("/contracts/{contractId}/analyze")
    public ResponseEntity<LawAnalyzeDto> analyzeContract(@PathVariable Long contractId) {
        try {
            LawAnalyzeDto result = lawService.analyzeLegalIssues(contractId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

}