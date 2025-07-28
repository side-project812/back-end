package com.qeat.domain.order.controller;

import com.qeat.domain.order.dto.request.QrRequest;
import com.qeat.domain.order.dto.response.QrResponse;
import com.qeat.domain.order.service.QrService;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Tag(name = "주문")
public class OrderController {
    private final QrService qrService;

    @PostMapping("/qr")
    @Operation(summary = "QR 코드 생성 API", description = "storeId를 기반으로 주문용 QR 코드 생성")
    public ResponseEntity<CustomResponse<QrResponse>> generateQr(@RequestBody QrRequest request) {
        QrResponse response = qrService.generateQr(request.getStoreId());
        return ResponseEntity.ok(CustomResponse.onSuccess(response, "QR 코드 생성 성공"));
    }
}
