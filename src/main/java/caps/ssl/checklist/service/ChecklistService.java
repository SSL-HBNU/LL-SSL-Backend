//package caps.ssl.checklist.service;
//
//
//import caps.ssl.checklist.dto.ChecklistCreateReqDto;
//import caps.ssl.checklist.dto.ChecklistResDto;
//import caps.ssl.checklist.model.Checklist;
//import caps.ssl.checklist.model.ChecklistItem;
//import caps.ssl.checklist.repository.ChecklistRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import caps.ssl.contract.model.Contract;
//import caps.ssl.contract.repository.ContractRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ChecklistService {
//    private final ChecklistRepository checklistRepository;
//    private final ContractRepository contractRepository;
//
//    @Transactional
//    public Long createChecklist(ChecklistCreateReqDto requestDto) {
//        Contract contract = contractRepository.findById(requestDto.getContractId())
//                .orElseThrow(() -> new IllegalArgumentException("계약서를 찾을 수 없습니다."));
//
//        checklistRepository.findByContractId(contract.getId()).ifPresent(c -> {
//            throw new IllegalStateException("이미 해당 계약서의 체크리스트가 존재합니다.");
//        });
//
//        List<ChecklistItem> items = requestDto.getItems().stream()
//                .map(itemDto -> ChecklistItem.builder()
//                        .itemNumber(itemDto.getItemNumber())
//                        .isChecked(itemDto.getIsChecked())
//                        .build())
//                .collect(Collectors.toList());
//
//        Checklist checklist = Checklist.createChecklist(contract, items);
//        Checklist savedChecklist = checklistRepository.save(checklist);
//
//        return savedChecklist.getId();
//    }
//
//    @Transactional(readOnly = true)
//    public ChecklistResDto getChecklistByContractId(Long contractId) {
//        Checklist checklist = checklistRepository.findByContractId(contractId)
//                .orElseThrow(() -> new IllegalArgumentException("체크리스트를 찾을 수 없습니다."));
//        return new ChecklistResDto(checklist);
//    }
//}
