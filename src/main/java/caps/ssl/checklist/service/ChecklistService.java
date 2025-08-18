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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ContractRepository contractRepository;
    private final OpenAiClient openAiClient;

    @Transactional
    public ChecklistResDto createChecklist(ChecklistCreateReqDto requestDto) {
        Contract contract = contractRepository.findById(requestDto.getContractId())
                .orElseThrow(() -> new IllegalArgumentException("계약서를 찾을 수 없습니다."));

        checklistRepository.findByContractId(contract.getId()).ifPresent(c -> {
            throw new IllegalStateException("이미 해당 계약서의 체크리스트가 존재합니다.");
        });

        String ocrText = contract.getOcrText();
        if (ocrText == null || ocrText.isBlank()) {
            throw new IllegalArgumentException("계약서 OCR 텍스트가 없습니다.");
        }

        // AI 분석 실행 (모든 항목에 대한 실제 계약서 내용 포함)
        List<ChecklistItem> aiItems = openAiClient.analyzeChecklist(ocrText);

        // 클라이언트가 보낸 체크 상태 적용
        Map<Integer, Boolean> clientChecks = requestDto.getItems().stream()
                .collect(Collectors.toMap(
                        ChecklistCreateReqDto.ChecklistItemReqDto::getItemNumber,
                        ChecklistCreateReqDto.ChecklistItemReqDto::getIsChecked
                ));

        List<ChecklistItem> finalItems = aiItems.stream()
                .map(aiItem -> {
                    Boolean isChecked = clientChecks.getOrDefault(aiItem.getItemNumber(), aiItem.isChecked());
                    return ChecklistItem.builder()
                            .itemNumber(aiItem.getItemNumber())
                            .isChecked(isChecked)
                            .guide(aiItem.getGuide())
                            .build();
                })
                .collect(Collectors.toList());

        Checklist checklist = Checklist.createChecklist(contract, finalItems);
        Checklist savedChecklist = checklistRepository.save(checklist);
        return new ChecklistResDto(savedChecklist);
    }

    @Transactional(readOnly = true)
    public ChecklistResDto getChecklistByContractId(Long contractId) {
        Checklist checklist = checklistRepository.findByContractId(contractId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트를 찾을 수 없습니다."));
        return new ChecklistResDto(checklist);
    }

}
