package caps.ssl.contract.client;

import caps.ssl.checklist.dto.ChecklistResDto;
import caps.ssl.contract.dto.Issue;
import caps.ssl.checklist.model.ChecklistItem;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // ----------------- 기존 불공정 조항 분석 -----------------
    public List<Issue> detectUnfairClauses(String text) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectNode systemMessage = objectMapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", """
            지금은 2025년입니다. 2025년 기준으로 아래 계약서 조항을 분석하여 불공정 조항(Issue) 여부와 이유를 알려주세요
            
            다음 근로계약서 문장에서 법적 문제가 있는 조항을 분류하세요. 반드시 아래 JSON 형식으로만 응답:
            
            {
              "issues": [
                {
                  "type": "퇴직금|최저임금|근로시간|부당해고|계약해지|기타",
                  "reason": "구체적인 법률 조항 포함 설명",
                  "evidence": "계약서 문장 중 정확한 인용문"
                }
              ]
            }
            
            응답은 JSON만 포함해야 하며 다른 텍스트는 금지됨""");

            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", text);

            ArrayNode messages = objectMapper.createArrayNode()
                    .add(systemMessage)
                    .add(userMessage);

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "gpt-4o");
            requestBody.set("messages", messages);
            requestBody.put("temperature", 0.2);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return parseIssuesFromResponse(response.getBody());
            } else {
                log.error("OpenAI 요청 실패: {}", response.getStatusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("detectUnfairClauses 예외 발생", e);
            return new ArrayList<>();
        }
    }

    private List<Issue> parseIssuesFromResponse(String responseBody) {
        List<Issue> issues = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            content = cleanJsonContent(content);

            JsonNode issuesRoot = objectMapper.readTree(content);
            JsonNode issuesNode = issuesRoot.path("issues");

            if (issuesNode.isArray()) {
                for (JsonNode node : issuesNode) {
                    issues.add(objectMapper.treeToValue(node, Issue.class));
                }
            }
        } catch (Exception e) {
            log.error("JSON 파싱 실패", e);
        }
        return issues;
    }

    // OpenAiClient.java
    public List<ChecklistItem> analyzeChecklist(String text) {
        String prompt = String.format("""
    다음 근로계약서를 아래 8개 체크리스트 항목 기준으로 분석해주세요.
    각 항목에 대해:
    1. isChecked: 계약서에 해당 항목이 명시되어 있는지 여부 (true/false)
    2. note: 항목의 상태와 실제 내용을 간결하게 표현
    
    반드시 아래 형식으로 note를 작성:
    - 항목이 명시된 경우: "[항목명]이 명시됨: [실제 내용]"
    - 항목이 명시되지 않은 경우: "[항목명]이 명시되지 않음"
    
    예시:
    {
      "itemNumber": 0,
      "isChecked": true,
      "note": "근무장소가 명시됨: 서울시 강남구 역삼동"
    }
    {
      "itemNumber": 1,
      "isChecked": false,
      "note": "수습 기간이 명시되지 않음"
    }
    
    반드시 JSON 형식으로 응답:
    
    {
      "items": [
        { "itemNumber": 0, "isChecked": true, "note": "근무장소가 명시됨: 서울시 강남구 역삼동" },
        { "itemNumber": 1, "isChecked": false, "note": "수습 기간이 명시되지 않음" },
        // ... 다른 항목들
      ]
    }
    
    체크리스트 항목:
    0. 근무장소
    1. 수습 기간
    2. 임금 조건
    3. 초과근무 수당
    4. 연차, 유급 휴가
    5. 퇴직금 지급 조건
    6. 4대 보험 가입 여부
    7. 기타 특약 조항

    --- 계약서 ---
    %s
    """, text);

        String response = getGptResponse(prompt);
        response = cleanJsonContent(response);
        log.info("OpenAI Checklist Response: {}", response);

        List<ChecklistItem> items = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode itemsNode = root.path("items");

            if (itemsNode.isArray()) {
                for (JsonNode node : itemsNode) {
                    items.add(ChecklistItem.builder()
                            .itemNumber(node.path("itemNumber").asInt())
                            .isChecked(node.path("isChecked").asBoolean())
                            .guide(node.path("note").asText()) // note를 guide로 바로 사용
                            .build());
                }
            }
        } catch (Exception e) {
            log.error("Checklist JSON 파싱 실패", e);
        }
        return items;
    }


    // Markdown 제거 함수
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


    // ----------------- GPT 호출 -----------------
    public String getGptResponse(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "gpt-4o");

            ArrayNode messages = objectMapper.createArrayNode();

            ObjectNode systemMessage = objectMapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", "당신은 계약서 분석 전문가입니다. 사용자의 질문에 대해 간결하고 명확하게 답변해주세요.");
            messages.add(systemMessage);

            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);

            requestBody.set("messages", messages);
            requestBody.put("temperature", 0.2);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("choices").get(0).path("message").path("content").asText();
            } else {
                log.error("OpenAI 요청 실패: {}", response.getStatusCode());
                throw new RuntimeException("OpenAI API 요청 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("GPT 처리 중 예외 발생", e);
            throw new RuntimeException("GPT 처리 실패", e);
        }
    }

    // ----------------- 계약서 요약 -----------------
    public String summarize(String text) {
        String prompt = String.format("""
                당신은 법률 문서를 일반인이 이해하기 쉽게 설명하는 전문가입니다.
                아래 '법률 원문'의 핵심 내용을 한국어로 2-4문장으로 요약하세요.
                최종 출력에는 요약문만 포함하고 다른 설명은 추가하지 마세요.
                
                --- 법률 원문 ---
                %s
                """, text);
        return getGptResponse(prompt);
    }
}
