package com.example.gastos_service.client;

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
    private final CircuitBreaker categoriaServiceCircuitBreaker;

    @Value("${services.categoria.url}")
    private String categoriaServiceUrl;

    public CategoriaDTO buscarCategoriaPorId(Long categoriaId, String token) {
        log.info("Buscando categoria com ID: {} através do Circuit Breaker", categoriaId);

        Supplier<CategoriaDTO> decoratedSupplier = CircuitBreaker
                .decorateSupplier(categoriaServiceCircuitBreaker, () -> {
                    return buscarCategoriaInternal(categoriaId, token);
                });

        try {
            return decoratedSupplier.get();
        } catch (Exception e) {
            log.error("Circuit Breaker ativo ou falha na busca da categoria: {}", e.getMessage());
            throw new CategoriaServiceUnavailableException(
                    "Serviço de categorias indisponível no momento. Tente novamente mais tarde.", e);
        }
    }

    private CategoriaDTO buscarCategoriaInternal(Long categoriaId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = categoriaServiceUrl + "/categorias/" + categoriaId;
            log.debug("Fazendo requisição para: {}", url);

            ResponseEntity<CategoriaDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    CategoriaDTO.class
            );

            log.info("Categoria encontrada com sucesso: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar categoria com ID: {}", categoriaId, e);
            throw new RuntimeException("Falha na comunicação com o serviço de categorias", e);
        }
    }

    public boolean categoriaExiste(Long categoriaId, String token) {
        log.info("Verificando existência da categoria com ID: {}", categoriaId);

        Supplier<Boolean> decoratedSupplier = CircuitBreaker
                .decorateSupplier(categoriaServiceCircuitBreaker, () -> {
                    CategoriaDTO categoria = buscarCategoriaInternal(categoriaId, token);
                    return categoria != null;
                });

        try {
            return decoratedSupplier.get();
        } catch (Exception e) {
            log.error("Circuit Breaker ativo ou falha na verificação da categoria: {}", e.getMessage());
            return false;
        }
    }
}