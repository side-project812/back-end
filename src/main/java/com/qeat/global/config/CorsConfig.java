package com.qeat.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://q-eat.store") // 또는 http://localhost:3000 등 프론트 도메인
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true) // JWT 쿠키 전송 시 필요
                .maxAge(3600);
    }
}
