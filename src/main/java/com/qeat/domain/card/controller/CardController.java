package com.qeat.domain.card.controller;

import com.qeat.domain.card.dto.request.CardRegisterRequest;
import com.qeat.domain.card.dto.response.CardListResponse;
import com.qeat.domain.card.dto.response.CardRegisterResponse;
import com.qeat.domain.card.service.CardService;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
@Tag(name = "카드")
public class CardController {

    private final CardService cardService;

    @PostMapping("/register")
    @Operation(summary = "카드 등록 API", description = "카드 등록 요청 처리")
    public ResponseEntity<CustomResponse<CardRegisterResponse>> registerCard(
            @RequestBody CardRegisterRequest request
    ) {
        CardRegisterResponse result = cardService.registerCard(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(result, "카드가 등록되었습니다."));
    }

    @GetMapping("/list")
    @Operation(summary = "카드 목록 조회 API", description = "카드 목록 조회 요청 처리")
    public ResponseEntity<CustomResponse<List<CardListResponse>>> getCardList() {
        List<CardListResponse> cards = cardService.getCardList();
        return ResponseEntity.ok(CustomResponse.onSuccess(cards, "등록된 카드 목록입니다."));
    }

    @DeleteMapping("/{cardId}")
    @Operation(summary = "카드 삭제 API", description = "카드 삭제 요청 처리")
    public ResponseEntity<CustomResponse<Void>> deleteCard(
            @PathVariable Long cardId) {
        cardService.deleteCard(cardId); // userId는 내부에서 추출
        return ResponseEntity.ok(CustomResponse.<Void>onSuccess(null, "카드가 삭제되었습니다."));
    }
}