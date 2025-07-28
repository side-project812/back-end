package com.qeat.domain.store.controller;

import com.qeat.domain.store.dto.response.StoreDetailResponse;
import com.qeat.domain.store.service.StoreService;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
@Tag(name = "Store", description = "가게")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{storeId}")
    @Operation(summary = "가게 상세 조회 API", description = "storeId를 기준으로 가게 상세정보를 조회합니다.")
    public ResponseEntity<CustomResponse<StoreDetailResponse>> getStoreDetail(@PathVariable Long storeId) {
        StoreDetailResponse result = storeService.getStoreDetail(storeId);
        return ResponseEntity.ok(CustomResponse.onSuccess(result));
    }
}