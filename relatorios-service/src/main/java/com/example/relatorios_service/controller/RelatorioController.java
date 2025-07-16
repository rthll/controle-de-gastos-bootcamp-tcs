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
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @PostMapping("/gastos/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPdf(@RequestBody RelatorioRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = auth.getName();

        byte[] pdfBytes = relatorioService.gerarRelatorioPdf();

        String filename = String.format("relatorio_gastos_%s_%s.pdf",
                request.getDataInicio().format(DATE_FORMATTER),
                request.getDataFim().format(DATE_FORMATTER)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/gastos/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPdfSimples(
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim,
            @RequestParam(defaultValue = "DETALHADO") String tipoRelatorio) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = auth.getName();

        RelatorioRequestDTO request = new RelatorioRequestDTO();
        request.setDataInicio(dataInicio);
        request.setDataFim(dataFim);
        request.setTipoRelatorio(tipoRelatorio);

        byte[] pdfBytes = relatorioService.gerarRelatorioPdf();

        String filename = String.format("relatorio_gastos_%s_%s.pdf",
                dataInicio.format(DATE_FORMATTER),
                dataFim.format(DATE_FORMATTER)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
