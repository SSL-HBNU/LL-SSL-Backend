package caps.ssl.contract.controller;

import caps.ssl.contract.model.Contract;
import caps.ssl.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contract")
public class ContractController {
    private final ContractService contractService;

//    @PostMapping(value = "/{memberId}/upload", consumes = "multipart/form-data")
//    public ResponseEntity<?> uploadAndAnalyzeContract(@PathVariable Long memberId, @RequestPart("file") MultipartFile file) throws Exception{
//        try{
//            Contract saved = contractService.save(memberId, file);
//            Map<String, Object> result = contractService.analyze(saved, file);
//            return ResponseEntity.ok(result);
//        } catch(Exception e){
//            return ResponseEntity.status(500).body("분석 오류: " + e.getMessage());
//        }
//    }


}
