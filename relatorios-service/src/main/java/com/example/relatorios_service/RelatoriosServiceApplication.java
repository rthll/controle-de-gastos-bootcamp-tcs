package com.example.relatorios_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RelatoriosServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RelatoriosServiceApplication.class, args);
	}
}
