package com.banking.digital_banking_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DigitalBankingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingPlatformApplication.class, args);
	}

}
