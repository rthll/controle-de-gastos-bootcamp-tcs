package com.example.relatorios_service.client;

import com.example.relatorios_service.dto.FuncionarioRelatorioDTO;
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
public class FuncionarioClient {

    private final RestTemplate restTemplate;

    @Value("${services.funcionarios.url}")
    private String funcionarioServiceUrl;

    public List<FuncionarioRelatorioDTO> buscarFuncionariosPorId(List<Long> funcionarioIds, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<List<Long>> entity = new HttpEntity<>(funcionarioIds, headers);

            String url = funcionarioServiceUrl + "/funcionarios/buscar-por-ids";

            ResponseEntity<List<FuncionarioRelatorioDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<FuncionarioRelatorioDTO>>() {}
            );

            System.out.println(response.getBody());

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar funcionarios", e);
            return null;
        }
    }
}