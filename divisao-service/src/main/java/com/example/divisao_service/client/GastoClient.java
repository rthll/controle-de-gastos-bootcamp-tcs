package com.example.divisao_service.client;

import com.example.divisao_service.dto.GastoDTO;
import com.example.divisao_service.dto.CategoriaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class GastoClient {

    private final RestTemplate restTemplate;

    @Value("${services.gastos.url}")
    private String gastoServiceUrl;

    @Value("${services.categoria.url}")
    private String categoriaServiceUrl;

    public GastoDTO buscarGastoPorId(UUID gastoId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = gastoServiceUrl + "/gastos/" + gastoId;

            ResponseEntity<GastoDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    GastoDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar ERRO AQUI categoria com ID: {}", gastoId, e);
            return null;
        }
    }

    public GastoDTO gastoExiste(UUID gastoId, String token) {
        return buscarGastoPorId(gastoId, token);
    }

    public GastoDTO divideGastos(GastoDTO gasto, String token){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GastoDTO> entity = new HttpEntity<>(gasto, headers);

            String url = gastoServiceUrl + "/gastos/divisao";

            ResponseEntity<GastoDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    GastoDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao Cadastrar o gasto: {}", e);
            return null;
        }
    }

    public CategoriaDTO criarCategoria(CategoriaDTO categoria, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CategoriaDTO> entity = new HttpEntity<>(categoria, headers);

            String url = categoriaServiceUrl + "/categorias";

            ResponseEntity<CategoriaDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    CategoriaDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao Cadastrar categoria para a divisao: {}", e);
            return null;
        }
    }

    public CategoriaDTO buscarCategoriaPorNome(String nome, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = categoriaServiceUrl + "/categorias/nome/" + nome;

            ResponseEntity<CategoriaDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    CategoriaDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro durante a divisao de gastos ao buscar categoria  com Nome: {}", nome, e);
            return null;
        }
    }

    public void deletarGastoById(UUID idgasto){
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = gastoServiceUrl + "/gastos/divisao/" + idgasto;

            ResponseEntity<GastoDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entity,
                    GastoDTO.class
            );

        } catch (Exception e) {
            log.error("Erro ao deletar gasto com ID: {}", idgasto, e);
        }
    }
}