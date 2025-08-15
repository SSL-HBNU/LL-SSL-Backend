package caps.ssl.law.controller;

import caps.ssl.law.dto.LawAnalyzeDto;
import caps.ssl.law.model.LawInfo;
import caps.ssl.law.service.LawService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LawController {
    private final LawService lawService;

    @GetMapping("/contracts/{contractId}/lawinfo")
    public List<LawInfo> getLawsByContract(@PathVariable Long contractId) {
        return lawService.getLawsByContractId(contractId);
    }

    @GetMapping("/lawinfo/{lawInfoId}")
    public LawInfo getLawDetail(@PathVariable Long lawInfoId) {
        return lawService.getLawById(lawInfoId);
    }

    @PostMapping("/contracts/{contractId}/analyze")
    public ResponseEntity<LawAnalyzeDto> analyzeContract(@PathVariable Long contractId) {
        try {
            LawAnalyzeDto result = lawService.analyzeLegalIssues(contractId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
