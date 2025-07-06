package com.example.gastos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GastoPorMesDTO {
    private YearMonth mes;
    private BigDecimal valor;
    private String mesFormatado;

    public GastoPorMesDTO(YearMonth mes, BigDecimal valor) {
        this.mes = mes;
        this.valor = valor;
        this.mesFormatado = mes.toString();
    }
}