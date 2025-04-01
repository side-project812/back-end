package com.qeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(
		scanBasePackages = {"com.qeat.admin", "com.qeat.core", "com.qeat.global"}
)
@ConfigurationPropertiesScan(basePackages = "com.qeat")
public class QeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(QeatApplication.class, args);
	}

}
