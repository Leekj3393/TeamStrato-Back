package com.strato.skylift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class StratoStsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StratoStsApplication.class, args);
	}

}
