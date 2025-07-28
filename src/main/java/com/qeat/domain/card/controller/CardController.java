package com.qeat.domain.card.controller;

import com.qeat.domain.card.dto.request.CardRegisterRequest;
import com.qeat.domain.card.dto.response.CardRegisterResponse;
import com.qeat.domain.card.service.CardService;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
@Tag(name = "카드")
public class CardController {

    private final CardService cardService;

    @PostMapping("/register")
    @Operation(summary = "카드 등록 API", description = "카드 등록 요청 처리")
    public ResponseEntity<CustomResponse<CardRegisterResponse>> registerCard(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody CardRegisterRequest request
    ) {
        CardRegisterResponse result = cardService.registerCard(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(result, "카드가 등록되었습니다."));
    }
}