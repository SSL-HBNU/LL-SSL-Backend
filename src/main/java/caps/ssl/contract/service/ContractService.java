package caps.ssl.contract.service;

import caps.ssl.contract.client.S3Uploader;
import caps.ssl.contract.model.Contract;
import caps.ssl.contract.repository.ContractRepository;
import caps.ssl.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final S3Uploader s3Uploader;
//    private final MemberService memberService;
//
//
//    public Map<String, Object>  analyze(Contract contract, MultipartFile file) throws Exception{
//        try{
//            String imagePath = s3Uploader.uploadFile(file, "images");
//
//            Member member = memberService.findById(contract.getMember().getId());
//
//        }
//    }
//
//    public Contract save(Long memberId, MultipartFile file) {
//        Member member = memberService.findById(memberId);
//        Contract contract = Contract.builder().member(member).build();
//
//        return contractRepository.save(contract);
//    }




}
