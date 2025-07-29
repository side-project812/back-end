package com.qeat.domain.order.controller;

import com.qeat.domain.order.dto.request.CartAddRequest;
import com.qeat.domain.order.dto.request.OrderRequest;
import com.qeat.domain.order.dto.request.QrRequest;
import com.qeat.domain.order.dto.response.CartAddResponse;
import com.qeat.domain.order.dto.response.CartResponse;
import com.qeat.domain.order.dto.response.OrderResponse;
import com.qeat.domain.order.dto.response.QrResponse;
import com.qeat.domain.order.service.CartService;
import com.qeat.domain.order.service.OrderService;
import com.qeat.domain.order.service.QrService;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Tag(name = "주문")
public class OrderController {
    private final QrService qrService;
    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/qr")
    @Operation(summary = "QR 코드 생성 API", description = "storeId를 기반으로 주문용 QR 코드 생성")
    public ResponseEntity<CustomResponse<QrResponse>> generateQr(@RequestBody QrRequest request) {
        QrResponse response = qrService.generateQr(request.getStoreId());
        return ResponseEntity.ok(CustomResponse.onSuccess(response, "QR 코드 생성 성공"));
    }

    @PostMapping("/cart")
    @Operation(summary = "장바구니 메뉴 추가 API", description = "장바구니에 메뉴를 추가합니다.")
    public ResponseEntity<CustomResponse<CartAddResponse>> addToCart(@RequestBody CartAddRequest request) {
        cartService.addCartItems(request);
        return ResponseEntity.ok(CustomResponse.<CartAddResponse>onSuccess(null, "장바구니에 담겼습니다."));
    }

    @GetMapping("/cart")
    @Operation(summary = "장바구니 조회 API", description = "storeId에 해당하는 장바구니를 조회합니다.")
    public ResponseEntity<CustomResponse<CartResponse>> getCart(@RequestParam Long storeId) {
        CartResponse response = cartService.getCartItems(storeId);
        return ResponseEntity.ok(CustomResponse.onSuccess(response, "장바구니 조회 성공"));
    }

    @PostMapping("/request")
    @Operation(summary = "주문 요청 API", description = "주문을 생성하고 결제 준비 정보를 반환합니다.")
    public ResponseEntity<CustomResponse<OrderResponse>> requestOrder(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(response, "결제 준비 완료"));
    }
}
