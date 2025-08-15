package caps.ssl.law.service;

import caps.ssl.contract.client.LawApiClient;
import caps.ssl.contract.client.OpenAiClient;
import caps.ssl.contract.model.Contract;
import caps.ssl.contract.model.ContractIssue;
import caps.ssl.contract.repository.ContractRepository;
import caps.ssl.law.dto.LawAnalyzeDto;
import caps.ssl.law.model.LawInfo;
import caps.ssl.law.repository.LawInfoRepository;
import caps.ssl.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LawService {

}
