package com.controlegastos.gastosservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelaDTO {

    private UUID id;
    private Integer numeroParcela;
    private BigDecimal valorParcela;
    private LocalDate dataVencimento;
}
