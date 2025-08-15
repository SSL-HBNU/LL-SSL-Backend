package caps.ssl.checklist.controller;


import caps.ssl.checklist.dto.ChecklistCreateReqDto;
import caps.ssl.checklist.dto.ChecklistResDto;
import caps.ssl.checklist.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checklists")
public class ChecklistController {

    private final ChecklistService checklistService;

    // 체크리스트 생성
    @PostMapping
    public ResponseEntity<?> createChecklist(@RequestBody ChecklistCreateReqDto requestDto) {
        try {
            Long checklistId = checklistService.createChecklist(requestDto);
            return ResponseEntity.created(URI.create("/api/checklists/" + checklistId)).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    // 계약서 ID로 체크리스트 조회
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<ChecklistResDto> getChecklist(@PathVariable Long contractId) {
        ChecklistResDto responseDto = checklistService.getChecklistByContractId(contractId);
        return ResponseEntity.ok(responseDto);
    }
}
