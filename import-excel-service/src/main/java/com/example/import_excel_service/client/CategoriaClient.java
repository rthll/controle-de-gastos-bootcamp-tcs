package com.example.import_excel_service.client;

import com.example.gastos_service.dto.CategoriaDTO;
import com.example.gastos_service.exception.CategoriaServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoriaClient {

    private final RestTemplate restTemplate;

    @Value("${services.categoria.url}")
    private String categoriaServiceUrl;




}