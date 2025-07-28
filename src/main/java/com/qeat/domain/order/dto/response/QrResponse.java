package com.qeat.domain.order.dto.response;

import lombok.Builder;

@Builder
public record QrResponse(
        String qrUrl,
        String qrImageBase64
) {}