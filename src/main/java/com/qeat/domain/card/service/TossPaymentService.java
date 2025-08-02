package com.qeat.domain.card.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qeat.global.config.TossProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final TossProperties tossProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String requestPayment(String billingKey, int amount, String orderId, String orderName, String customerEmail) {
        String url = tossProperties.getBaseUrl() + "/v1/billing/" + billingKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(encodeSecretKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", amount);
        payload.put("orderId", orderId);
        payload.put("orderName", orderName);
        payload.put("customerEmail", customerEmail);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = new RestTemplate().postForEntity(url, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            log.error("Toss 결제 실패: {}", response);
            throw new RuntimeException("Toss 결제 요청 실패");
        }
    }

    private String encodeSecretKey() {
        return Base64.getEncoder()
                .encodeToString((tossProperties.getSecretKey() + ":").getBytes(StandardCharsets.UTF_8));
    }
}