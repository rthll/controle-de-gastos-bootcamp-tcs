package com.example.funcionarios_service.client;

import com.example.funcionarios_service.dto.SetorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetorClient {

    private final RestTemplate restTemplate;

    @Value("${services.setor.url}")
    private String setorServiceUrl;

    public SetorDTO buscarSetorPorId(Long setorId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = setorServiceUrl + "/setores/" + setorId;

            ResponseEntity<SetorDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SetorDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar setor com ID: {}", setorId, e);
            return null;
        }
    }

    public boolean setorExiste(Long setorId, String token) {
        SetorDTO setor = buscarSetorPorId(setorId, token);
        return setor != null;
    }
}

