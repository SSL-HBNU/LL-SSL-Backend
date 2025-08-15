//package caps.ssl.checklist.controller;
//
//
//import caps.ssl.checklist.dto.ChecklistCreateReqDto;
//import caps.ssl.checklist.dto.ChecklistResDto;
//import caps.ssl.checklist.service.ChecklistService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/checklists")
//public class ChecklistController {
//
//    private final ChecklistService checklistService;
//
//    // 체크리스트 생성
//    @PostMapping
//    public ResponseEntity<Void> createChecklist(@RequestBody ChecklistCreateReqDto requestDto) {
//        Long checklistId = checklistService.createChecklist(requestDto);
//        return ResponseEntity.created(URI.create("/api/checklists/" + checklistId)).build();
//    }
//
//    // 계약서 ID로 체크리스트 조회
//    @GetMapping("/contract/{contractId}")
//    public ResponseEntity<ChecklistResDto> getChecklist(@PathVariable Long contractId) {
//        ChecklistResDto responseDto = checklistService.getChecklistByContractId(contractId);
//        return ResponseEntity.ok(responseDto);
//    }
//}
