package com.example.relatorios_service.controller;

import com.example.relatorios_service.dto.RelatorioRequestDTO;
import com.example.relatorios_service.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;
    private static final DateTimeFormatter formatarDataHora = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");


    @PostMapping("/gastos/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPdf(@RequestBody RelatorioRequestDTO relatorioRequest) {

        byte[] pdfBytes = relatorioService.gerarRelatorioPdf(relatorioRequest);

        String datahora = LocalDateTime.now().format(formatarDataHora);
        String filename = String.format("relatorio_gastos_%s.pdf", datahora);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
