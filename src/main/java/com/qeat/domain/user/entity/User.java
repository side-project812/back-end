package com.qeat.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String provider;    // ex) "LOCAL", "GOOGLE", "APPLE"
    private String providerId;  // 소셜 로그인 식별자 (LOCAL은 null 가능)
    private String avatar;

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    @Column(name = "payment_password")
    private String paymentPassword;

    // 결제 비밀번호 수정 메소드
    public void changePaymentPassword(String newPassword) {
        this.paymentPassword = newPassword;
    }

    // 닉네임 변경 메소드
    public void changeName(String newName) {
        this.name = newName;
    }
}