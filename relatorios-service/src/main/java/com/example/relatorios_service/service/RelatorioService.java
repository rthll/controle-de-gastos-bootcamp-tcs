package com.example.relatorios_service.service;

import com.example.relatorios_service.client.FuncionarioClient;
import com.example.relatorios_service.client.GastoClient;
import com.example.relatorios_service.dto.FuncionarioRelatorioDTO;
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
    private final FuncionarioClient funcionarioClient;

    public byte[] gerarRelatorioPdf(RelatorioRequestDTO relatorioRequest) {

        try {
            String token = tokenService.getCurrentToken();
            System.out.println("Token: " + token);
            List<GastoRelatorioDTO> gastosSelecionados = gastoClient.buscarGastosPorIds(relatorioRequest.getGastoIds(), token);

            log.info("Encontrados {} gastos para o relatório", gastosSelecionados.size());

            // Gera o PDF
            return pdfService.gerarRelatorioPdf(gastosSelecionados);

        } catch (IOException e) {
            log.error("Erro ao gerar relatório PDF: ", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }

    public byte[] gerarRelatorioPdfFuncionarios(RelatorioRequestDTO relatorioRequest) {

        try {
            String token = tokenService.getCurrentToken();
            System.out.println("Token: " + token);
            List<FuncionarioRelatorioDTO> funcionariosSelecionados = funcionarioClient.buscarFuncionariosPorId(relatorioRequest.getGastoIds(), token);

            log.info("Encontrados {} funcionarios para o relatório", funcionariosSelecionados.size());

            // Gera o PDF
            return pdfService.gerarRelatorioPdfFuncionario(funcionariosSelecionados);

        } catch (IOException e) {
            log.error("Erro ao gerar relatório PDF: ", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }
}