package com.qeat.global.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    // 비밀번호 재설정 인증번호 전송
    public String sendPasswordResetCode(String toEmail) {
        String code = generateCode(); // 4자리

        // 템플릿에 값 삽입
        Context context = new Context();
        context.setVariable("code", code);
        String htmlContent = templateEngine.process("email/password-reset", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[Qeat] 비밀번호 재설정 인증번호");
            helper.setText(htmlContent, true); // HTML 내용 적용

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }

        return code;
    }

    // 결제 비밀번호 재설정 인증번호 전송
    public String sendPaymentPasswordResetCode(String toEmail) {
        String code = generateCode(); // 예: 4자리

        try {
            Context context = new Context();
            context.setVariable("code", code);

            String htmlContent = templateEngine.process("email/payment-reset", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[Qeat] 결제 비밀번호 재설정 인증번호");
            helper.setText(htmlContent, true);

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