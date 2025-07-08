package com.example.gastos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoRequestDTO {

    private UUID id;
    private String descricao;
    private BigDecimal valorTotal;
    private LocalDate data;
    private boolean parcelado;
    private Integer numeroParcelas;
    private String fonte;
    private UUID categoriaId;
}