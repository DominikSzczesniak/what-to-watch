package pl.szczesniak.dominik.whattowatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

@SpringBootApplication
//@EnableJpaAuditing
//@EnableGlobalAuthentication
public class WhatToWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhatToWatchApplication.class, args);
	}

}
