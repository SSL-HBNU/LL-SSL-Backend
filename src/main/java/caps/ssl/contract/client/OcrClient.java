package caps.ssl.contract.client;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class OcrClient {
    @Value("${upstage.api.key}")
    private String apiKey;

    @Value("{upstage.api.url}")
    private String ocrApiUrl;

    private final RestTemplate restTemplate;

    public JSONObject extractOcrJson(MultipartFile file){

    }


}
