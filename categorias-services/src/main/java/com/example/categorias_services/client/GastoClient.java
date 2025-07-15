package com.example.categorias_services.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GastoClient {

    private final RestTemplate restTemplate;

    @Value("${services.gasto.url}")
    private String gastoServiceUrl;

    public boolean existeGastoComCategoria(Long categoriaId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = gastoServiceUrl + "/gastos/existe-categoria/" + categoriaId;

            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Boolean.class
            );

            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            log.error("Erro ao verificar existência de gastos para categoria: {}", categoriaId, e);
            return true;
        }
    }
}