package caps.ssl.contract.client;

import caps.ssl.contract.model.ContractIssue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiClient {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<ContractIssue> detectUnfairClauses(String text) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectNode message1 = objectMapper.createObjectNode();
        message1.put("role", "system");
        message1.put("content", """
                프롬포트
                """);

        ObjectNode message2 = objectMapper.createObjectNode();
        message2.put("role", "user");
        message2.put("content", text);

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "gpt-4o");
        requestBody.set("messages", objectMapper.createArrayNode().add(message1).add(message2));
        requestBody.put("temperature", 0.2);

        ObjectNode responseFormat = objectMapper.createObjectNode();
        responseFormat.put("type", "json_object");
        requestBody.set("response_format", responseFormat);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        ResponseEntity<String> response = restTemplate.exchange(openaiApiUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = parseJson(response.getBody());
            return extractIssues(jsonNode);
        } else {
            throw new Exception("OpenAI 요청 실패: " + response.getStatusCode());
        }
    }

    public String summarize(String text) {
        String prompt = String.format("""
            당신은 법률 문서를 일반인이 이해하기 쉽게 설명하는 전문가입니다.
            당신의 임무는 아래 '법률 원문'의 핵심 내용을 먼저 한국어로 2-4문장으로 요약한 뒤, 최종 결과를 제공하는 것입니다.
            최종 응답에는 번역된 요약문만 포함해야 하며, 다른 설명은 절대 추가하지 마세요.
            
            --- 법률 원문 ---
            %s
            """, text);
        return getGptResponse(prompt);
    }

    private String getGptResponse(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "gpt-4o");

            ArrayNode messages = objectMapper.createArrayNode();
            ObjectNode message = objectMapper.createObjectNode();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);

            requestBody.set("messages", messages);
            requestBody.put("temperature", 0.2);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.error("GPT 처리 중 예외 발생", e);
            throw new RuntimeException("GPT 처리 실패", e);
        }
    }

    private JsonNode parseJson(String responseBody) {
        try {
            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 오류", e);
        }
    }

    private List<ContractIssue> extractIssues(JsonNode root) {
        List<ContractIssue> issues = new ArrayList<>();
        try {
            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.isEmpty()) return issues;
            String content = choices.get(0).path("message").path("content").asText();
            if (content == null || content.isEmpty()) return issues;
            content = cleanJsonContent(content);
            JsonNode issuesRoot = objectMapper.readTree(content);
            JsonNode issuesNode = issuesRoot.path("issues");
            if (issuesNode.isArray()) {
                for (JsonNode issueNode : issuesNode) {
                    issues.add(objectMapper.treeToValue(issueNode, ContractIssue.class));
                }
            }
        } catch (Exception e) {
            log.error("이슈 추출 중 JSON 파싱 실패", e);
        }
        return issues;
    }

    private String cleanJsonContent(String content) {
        content = content.trim();
        if (content.startsWith("```json")) {
            return content.substring(7, content.length() - 3).trim();
        }
        if (content.startsWith("```")) {
            return content.substring(3, content.length() - 3).trim();
        }
        return content;
    }


}
