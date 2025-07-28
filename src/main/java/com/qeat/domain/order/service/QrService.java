package com.qeat.domain.order.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.qeat.domain.order.dto.response.QrResponse;
import com.qeat.domain.order.exception.code.OrderErrorCode;
import com.qeat.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrService {

    public QrResponse generateQr(Long storeId) {
        try {
            String url = "https://your-app.com/store/" + storeId;

            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

            String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            String base64WithHeader = "data:image/png;base64," + base64Image;

            return QrResponse.builder()
                    .qrUrl(url)
                    .qrImageBase64(base64WithHeader)
                    .build();

        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.STORE_NOT_FOUND);
        }
    }
}