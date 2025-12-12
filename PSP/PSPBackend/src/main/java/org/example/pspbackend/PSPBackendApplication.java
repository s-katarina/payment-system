package org.example.pspbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PSPBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PSPBackendApplication.class, args);
	}

}

