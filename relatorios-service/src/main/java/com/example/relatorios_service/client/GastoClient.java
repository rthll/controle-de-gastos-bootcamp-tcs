package com.example.relatorios_service.client;

import com.example.relatorios_service.dto.GastoRelatorioDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GastoClient {

    private final RestTemplate restTemplate;

    @Value("${services.gastos.url}")
    private String gastoServiceUrl;

    public List<GastoRelatorioDTO> listarGastos(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = gastoServiceUrl + "/gastos";

            // Deserializar para List<GastoResponseDTO>
            ResponseEntity<List<GastoRelatorioDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<GastoRelatorioDTO>>() {}
            );

            System.out.println(response.getBody());

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar gastos", e);
            return null;
        }
    }
}