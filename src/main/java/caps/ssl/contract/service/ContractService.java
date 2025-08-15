package caps.ssl.contract.service;

import caps.ssl.contract.client.OcrClient;
import caps.ssl.contract.client.S3Uploader;
import caps.ssl.contract.exception.InvalidContractException;
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

    // 계약서 판별을 위한 핵심 키워드 목록
    private static final List<String> CONTRACT_KEYWORDS = List.of(
            "계약서", "근로계약", "임대차계약", "매매계약", "갑 을", "계약기간",
            "계약일자", "계약자", "피계약자", "계약금", "중개사", "공인중개사",
            "근로자", "사용자", "임금", "근무시간", "계약조건", "서명일", "계약목적",
            "소유자", "임차인", "임대인", "매도인", "매수인", "보증금", "월세"
    );

    public Map<String, Object> analyze(Contract contract, MultipartFile contractImage) {
        try {
            // 1. OCR 수행
            JSONObject ocrResponse = ocrClient.extractOcrJson(contractImage);
            String fullText = ocrResponse.getString("text");

            // 2. 계약서 유효성 검증
            validateContractContent(fullText);

            // 3. 검증 통과 시 S3 업로드
            String originalImageUrl = s3Uploader.uploadFile(contractImage, "ll/originals");

            // 4. 계약 정보 저장
            contract.setImagePath(originalImageUrl);
            contract.setOcrText(fullText);
            contractRepository.save(contract);

            return Map.of(
                    "contractId", contract.getId(),
                    "originalImage", originalImageUrl,
                    "text", fullText
            );

        } catch (InvalidContractException ice) {
            throw ice; // 계약서 관련 예외는 상위로 전파
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("OCR 분석 오류: " + e.getMessage());
        }
    }

    // 계약서 내용 검증 메서드
    private void validateContractContent(String fullText) {
        String normalizedText = fullText.toLowerCase().replaceAll("\\s+", "");
        int keywordCount = 0;

        for (String keyword : CONTRACT_KEYWORDS) {
            String normalizedKeyword = keyword.toLowerCase().replaceAll("\\s+", "");
            if (normalizedText.contains(normalizedKeyword)) {
                keywordCount++;
                if (keywordCount >= 3) { // 최소 3개 이상 키워드 검출 시 계약서로 판단
                    break;
                }
            }
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