package com.portfolio.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Esta anotação ativa o ecossistema do Spring Boot
public class ApiApplication {

	public static void main(String[] args) {
		// Esta linha inicia o servidor Tomcat embutido e carrega a aplicação
		SpringApplication.run(ApiApplication.class, args);
	}
}