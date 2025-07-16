package com.example.setor_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FuncionarioClient {

    private final RestTemplate restTemplate;

    @Value("${services.funcionario.url}")
    private String funcionariosServiceUrl;

    public boolean existeFuncionarioComSetor(Long setorId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = funcionariosServiceUrl + "/gastos/existe-categoria/" + setorId;

            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Boolean.class
            );

            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            log.error("Erro ao verificar existência de gastos para categoria: {}", setorId, e);
            return true;
        }
    }



}
