package com.example.import_excel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoRequestDTO {

    private Long id;
    private String descricao;
    private BigDecimal valorTotal;
    private LocalDateTime data;
    private boolean parcelado;
    private Integer numeroParcelas;
    private String fonte;
    private Long categoriaId;
    private String usuarioId;
}