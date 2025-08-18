package caps.ssl.checklist.controller;

import caps.ssl.checklist.dto.ChecklistCreateReqDto;
import caps.ssl.checklist.dto.ChecklistResDto;
import caps.ssl.checklist.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @PostMapping
    public ResponseEntity<ChecklistResDto> createChecklist(@RequestBody ChecklistCreateReqDto requestDto) {
        ChecklistResDto checklist = checklistService.createChecklist(requestDto);
        return ResponseEntity.ok(checklist);
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<ChecklistResDto> getChecklistByContractId(@PathVariable Long contractId) {
        ChecklistResDto checklist = checklistService.getChecklistByContractId(contractId);
        return ResponseEntity.ok(checklist);
    }
}
