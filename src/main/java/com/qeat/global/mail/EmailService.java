package com.qeat.global.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 비밀번호 재설정 인증번호 전송
    public String sendPasswordResetCode(String toEmail) {
        String code = generateCode(); // 6자리 숫자 인증코드 생성

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[Qeat] 비밀번호 재설정 인증번호");
            helper.setText(
                    "<div style='font-family:Arial,sans-serif;'>" +
                            "<h2>Qeat 비밀번호 재설정</h2>" +
                            "<p>아래 인증번호를 입력해주세요:</p>" +
                            "<h3 style='color:blue'>" + code + "</h3>" +
                            "<p>인증번호는 4분간 유효합니다.</p>" +
                            "</div>",
                    true
            );

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }

        return code;
    }

    public String sendPaymentPasswordResetCode(String toEmail) {
        String code = generateCode(); // 4자리

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[Qeat] 결제 비밀번호 재설정 인증번호");
            helper.setText(
                    "<h3>Qeat 결제 비밀번호 재설정</h3><p>인증번호: <strong>" + code + "</strong></p><p>유효시간: 4분</p>",
                    true
            );
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }

        return code;
    }

    // 4자리 랜덤 숫자 생성
    private String generateCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // 1000 ~ 9999
        return String.valueOf(code);
    }
}