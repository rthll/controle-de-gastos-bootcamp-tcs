package com.example.gastos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoMesDTO {
    private String descricao;
    private BigDecimal valor;
    private String categoria;
}

