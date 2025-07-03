package com.example.gastos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoRequestDTO {

    private String descricao;

    private BigDecimal valorTotal;

    private LocalDate data;

    private boolean parcelado;

    private Integer numeroParcelas;

    private String fonte;

    private String categoria;
}
