package com.example.import_excel_service.client;

import com.example.import_excel_service.dto.GastoImportDTO;
import com.example.import_excel_service.dto.GastoRequestDTO;
import com.example.import_excel_service.dto.GastoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public List<GastoImportDTO> criarGasto(GastoRequestDTO dto, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<GastoRequestDTO> entity = new HttpEntity<>(dto, headers);

            String url = gastoServiceUrl + "/gastos";

            ResponseEntity<List<GastoImportDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<GastoImportDTO>>() {}
            );

            System.out.println(response.getBody());

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar gastos", e);
            return null;
        }
    }
}