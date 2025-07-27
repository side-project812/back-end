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
}