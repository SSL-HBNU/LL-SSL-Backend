package caps.ssl.contract.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class LawApiClient {

    @Value("${law.api.key}")
    private String apiKey;

    @Value("{law.api.oc}")
    private String oc;

    @Value("law.api.url.search")
    private String lawSearchApiUrl;

    @Value("law.api.url.search")
    private String lawServiceApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


}
