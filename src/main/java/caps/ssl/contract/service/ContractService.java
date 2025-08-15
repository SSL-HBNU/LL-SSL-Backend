package caps.ssl.contract.service;

import caps.ssl.contract.client.OcrClient;
import caps.ssl.contract.client.S3Uploader;
import caps.ssl.contract.model.Contract;
import caps.ssl.contract.repository.ContractRepository;
import caps.ssl.member.model.Member;
import caps.ssl.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final S3Uploader s3Uploader;
    private final MemberService memberService;
    private final OcrClient ocrClient;


    public Map<String, Object> analyze(Contract contract, MultipartFile contractImage) {
        try {
            String originalImageUrl = s3Uploader.uploadFile(contractImage, "ll/originals");

            JSONObject ocrResponse = ocrClient.extractOcrJson(contractImage);

            String fullText = ocrResponse.getString("text");

            contract.setImagePath(originalImageUrl);
            contract.setOcrText(fullText);
            contractRepository.save(contract);

            return Map.of(
                    "contractId", contract.getId(),
                    "originalImage", originalImageUrl,
                    "text", fullText
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("OCR 분석 오류 : " + e.getMessage());
        }
    }


    public Contract save(Long memberId, MultipartFile file) {
        Member member = memberService.findById(memberId);
        Contract contract = Contract.builder().member(member).build();

        return contractRepository.save(contract);
    }

    public List<Contract> findAllByMemberId(Long memberId) {
        return contractRepository.findByMemberId(memberId);
    }

    public Contract findById(Long contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("계약서를 찾을 수 없습니다."));
    }

}
