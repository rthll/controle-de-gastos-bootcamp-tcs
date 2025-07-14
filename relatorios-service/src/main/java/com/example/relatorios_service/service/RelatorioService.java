package com.example.relatorios_service.service;

import com.example.relatorios_service.client.GastoClient;
import com.example.relatorios_service.dto.GastoRelatorioDTO;
import com.example.relatorios_service.dto.RelatorioRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatorioService {

    private final GastoClient gastoClient;
    private final PdfService pdfService;
    private final TokenService tokenService;

    public byte[] gerarRelatorioPdf() {
        try {
            String token = tokenService.getCurrentToken();
            System.out.println("Token: " + token);
            List<GastoRelatorioDTO> gastos = gastoClient.listarGastos(
                    token
            );

            log.info("Encontrados {} gastos para o relatório", gastos.size());

            // Gera o PDF
            return pdfService.gerarRelatorioPdf(
                    gastos
            );

        } catch (IOException e) {
            log.error("Erro ao gerar relatório PDF: ", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }
}