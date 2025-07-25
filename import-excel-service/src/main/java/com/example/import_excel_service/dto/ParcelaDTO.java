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
public class ParcelaDTO {

    private Long id;
    private Integer numeroParcela;
    private BigDecimal valorParcela;
    private LocalDateTime dataVencimento;
}
