package com.example.relatorios_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class GastoRelatorioDTO {
    private UUID id;
    private boolean ativo;
    private String descricao;
    private BigDecimal valorTotal;
    private LocalDateTime data;
    private boolean parcelado;
    private Integer numeroParcelas;
    private String usuarioId;
    private String fonte;
    private UUID categoriaId;
    private CategoriaDTO categoria;
    private List<ParcelaDTO> parcelas;
}