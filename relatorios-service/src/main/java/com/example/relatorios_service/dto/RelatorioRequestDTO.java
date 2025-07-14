package com.example.relatorios_service.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RelatorioRequestDTO {
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String categoriaId;
    private String tipoRelatorio;
}