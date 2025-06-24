package org.example.email_assistant.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.email_assistant.Controller.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiAPIUrl;
    @Value("${gemini.api.key}")
    private String geminiAPIKey;
    @PostConstruct
    public void init() {
        System.out.println("geminiAPIUrl = [" + geminiAPIUrl + "]");
        System.out.println("geminiAPIKey = [" + geminiAPIKey + "]");
    }
    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }


    public String generateResponse(EmailRequest emailRequest) {
        String prompt = buildPrompt(emailRequest);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        String response = webClient.post()
                .uri(geminiAPIUrl + "?key=" + geminiAPIKey) // note the '?key='
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        return extractTheResponse(response);
    }


    private String extractTheResponse(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing the request!";
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional reply for the following email content, please don't generate a subject line");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append(" and the following tone is: ").append(emailRequest.getTone());
        }
        prompt.append("\nOriginal Email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
