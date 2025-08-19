package caps.ssl.law.service;

import caps.ssl.contract.client.LawApiClient;
import caps.ssl.contract.client.OpenAiClient;
import caps.ssl.contract.dto.Issue;
import caps.ssl.contract.model.Contract;
import caps.ssl.contract.repository.ContractRepository;
import caps.ssl.law.dto.LawAnalyzeDto;
import caps.ssl.law.dto.LawInfoDTO;
import caps.ssl.law.model.LawInfo;
import caps.ssl.law.repository.LawInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LawService {

    private final LawInfoRepository lawInfoRepository;
    private final OpenAiClient openAiClient;
    private final LawApiClient lawApiClient;
    private final ContractRepository contractRepository;

    public LawAnalyzeDto analyzeLegalIssues(Long contractId) throws Exception {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약서 없음"));

        List<Issue> issues = openAiClient.detectUnfairClauses(contract.getOcrText());
        Map<Issue, List<LawInfo>> issueLawMap = new HashMap<>();

        for (Issue issue : issues) {
            List<LawInfo> laws = lawApiClient.searchRelatedLaws(issue.getType(), contract);

            laws.forEach(law -> {
                try {
                    String lawContent = lawApiClient.fetchLawDetailByApi(law.getLawSerialNumber());
                    if (!StringUtils.hasText(lawContent)) {
                        log.warn("법률 '{}'의 상세 내용을 API로 가져오지 못했습니다. 분석을 건너뜁니다.", law.getLawName());
                        return;
                    }
                    String summary = openAiClient.summarize(lawContent);
                    law.setSummary(summary);
                    law.setContract(contract);
                } catch (Exception e) {
                    log.error("법령 상세 조회 또는 AI 처리 실패: law={}, error={}", law.getLawName(), e.getMessage());
                }
            });

            try {
                List<LawInfo> validLaws = laws.stream()
                        .filter(l -> l.getSummary() != null && !l.getSummary().isBlank())
                        .collect(Collectors.toList());
                lawInfoRepository.saveAll(validLaws);
                issueLawMap.put(issue, validLaws);
            } catch (DataIntegrityViolationException e) {
                log.error("DB 저장 실패: {}", e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage());
                throw new RuntimeException("법령 정보 저장 실패", e);
            }
        }

        return new LawAnalyzeDto(
                contract.getId(),
                issues,
                issueLawMap.values().stream()
                        .flatMap(List::stream)
                        .map(LawInfoDTO::new)
                        .collect(Collectors.toList())
        );
    }

    public List<LawInfo> getLawsByContractId(Long contractId) {
        return lawInfoRepository.findByContractId(contractId);
    }

    public LawInfo getLawById(Long lawInfoId) {
        return lawInfoRepository.findById(lawInfoId)
                .orElseThrow(() -> new IllegalArgumentException("법률 정보를 찾을 수 없습니다."));
    }


}