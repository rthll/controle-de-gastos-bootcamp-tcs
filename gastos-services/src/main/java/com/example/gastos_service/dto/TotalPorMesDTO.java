package com.example.gastos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TotalPorMesDTO {
    private String mes;
    private BigDecimal total;
}
