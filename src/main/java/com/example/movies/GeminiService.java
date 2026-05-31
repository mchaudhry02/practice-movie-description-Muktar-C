package com.example.movies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class GeminiService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String generateDescription(String movieTitle) {
        String url = "https://openrouter.ai/api/v1/chat/completions";

        // Build request body
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Write a 2-3 sentence movie description for: " + movieTitle);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "google/gemini-2.0-flash-001");
        body.put("messages", List.of(message));

        // Send request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        Map.class
                );

        Map response = responseEntity.getBody();

        // Parse response
        try {
            List choices = (List) response.get("choices");
            Map choice = (Map) choices.get(0);
            Map messageResp = (Map) choice.get("message");
            return (String) messageResp.get("content");
        } catch (Exception e) {
            return "Description could not be generated.";
        }
    }
}