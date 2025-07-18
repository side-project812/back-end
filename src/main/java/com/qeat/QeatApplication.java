package com.qeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //JPA Auditing 기능 활성화
@SpringBootApplication(
		scanBasePackages = {"com.qeat.global"}
)
@ConfigurationPropertiesScan(basePackages = "com.qeat")
public class QeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(QeatApplication.class, args);
	}

}
