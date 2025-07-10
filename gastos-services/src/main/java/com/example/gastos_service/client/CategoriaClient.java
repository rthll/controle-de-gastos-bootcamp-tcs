package com.example.gastos_service.client;

import com.example.gastos_service.dto.CategoriaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoriaClient {

    private final RestTemplate restTemplate;

    @Value("${services.categoria.url}")
    private String categoriaServiceUrl;

    public CategoriaDTO buscarCategoriaPorId(UUID categoriaId, String token) {
        System.out.println(token);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = categoriaServiceUrl + "/categorias/" + categoriaId;

            ResponseEntity<CategoriaDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    CategoriaDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar categoria com ID: {}", categoriaId, e);
            return null;
        }
    }

    public boolean categoriaExiste(UUID categoriaId, String token) {
        CategoriaDTO categoria = buscarCategoriaPorId(categoriaId, token);
        return categoria != null;
    }
}