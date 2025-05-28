package com.umeume.umeumesweets.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TranslateController {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final int MAX_SEGMENTS = 128;

    @PostMapping("/translate")
    public List<String> translate(@RequestBody Map<String, Object> payload) {
        List<String> texts = (List<String>) payload.get("texts");
        String target = (String) payload.get("target");

        List<String> translatedResults = new ArrayList<>();

        for (int i = 0; i < texts.size(); i += MAX_SEGMENTS) {
            List<String> batch = texts.subList(i, Math.min(i + MAX_SEGMENTS, texts.size()));
            translatedResults.addAll(callTranslateApi(batch, target));
        }

        return translatedResults;
    }

    private List<String> callTranslateApi(List<String> texts, String target) {
        String url = "https://translation.googleapis.com/language/translate/v2?key=" + apiKey;

        Map<String, Object> body = new HashMap<>();
        body.put("q", texts);
        body.put("target", target);
        body.put("format", "text");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        Map response = restTemplate.postForObject(url, entity, Map.class);
        List<Map> translations = (List<Map>) ((Map) response.get("data")).get("translations");

        return translations.stream()
                .map(t -> (String) t.get("translatedText"))
                .collect(Collectors.toList());
    }
}
