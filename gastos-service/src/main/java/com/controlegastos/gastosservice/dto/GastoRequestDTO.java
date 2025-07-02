package com.controlegastos.gastosservice.dto;

import lombok.*;

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
}
