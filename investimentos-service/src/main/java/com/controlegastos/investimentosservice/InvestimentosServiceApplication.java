package com.controlegastos.investimentosservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InvestimentosServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvestimentosServiceApplication.class, args);
    }
}
