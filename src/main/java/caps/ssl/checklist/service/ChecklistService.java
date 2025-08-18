package caps.ssl.checklist.service;

import caps.ssl.checklist.dto.ChecklistCreateReqDto;
import caps.ssl.checklist.dto.ChecklistResDto;
import caps.ssl.checklist.model.Checklist;
import caps.ssl.checklist.model.ChecklistItem;
import caps.ssl.checklist.repository.ChecklistRepository;
import caps.ssl.contract.client.OpenAiClient;
import caps.ssl.contract.model.Contract;
import caps.ssl.contract.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ContractRepository contractRepository;
    private final OpenAiClient openAiClient;

    @Transactional
    public ChecklistResDto createChecklist(ChecklistCreateReqDto requestDto) {

        if (requestDto.getItems() == null || requestDto.getItems().isEmpty()) {
            throw new IllegalArgumentException("체크리스트 항목이 비어있습니다.");
        }

        if (requestDto.getItems().size() != 8) {
            throw new IllegalArgumentException("체크리스트 항목은 반드시 8개여야 합니다.");
        }

        Contract contract = contractRepository.findById(requestDto.getContractId())
                .orElseThrow(() -> new IllegalArgumentException("계약서를 찾을 수 없습니다."));

        checklistRepository.findByContractId(contract.getId()).ifPresent(c -> {
            throw new IllegalStateException("이미 해당 계약서의 체크리스트가 존재합니다.");
        });

        String ocrText = contract.getOcrText();
        if (ocrText == null || ocrText.isBlank()) {
            throw new IllegalArgumentException("계약서 OCR 텍스트가 없습니다.");
        }

        // OpenAI에서 체크리스트 분석
        List<ChecklistItem> items = openAiClient.analyzeChecklist(ocrText);

        // Checklist 엔티티 생성 및 저장
        Checklist checklist = Checklist.createChecklist(contract, items);
        Checklist savedChecklist = checklistRepository.save(checklist);

        // ChecklistResDto 반환
        return new ChecklistResDto(savedChecklist);
    }

    @Transactional(readOnly = true)
    public ChecklistResDto getChecklistByContractId(Long contractId) {
        Checklist checklist = checklistRepository.findByContractId(contractId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트를 찾을 수 없습니다."));
        return new ChecklistResDto(checklist);
    }

}
