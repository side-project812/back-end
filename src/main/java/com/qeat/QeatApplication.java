package com.qeat;

import com.qeat.global.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableJpaAuditing //JPA Auditing 기능 활성화
@SpringBootApplication(
		scanBasePackages = {"com.qeat"}
)
@ConfigurationPropertiesScan(basePackages = "com.qeat")
public class QeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(QeatApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}