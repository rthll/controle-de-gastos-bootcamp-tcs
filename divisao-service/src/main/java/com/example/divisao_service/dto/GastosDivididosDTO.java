package com.example.divisao_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastosDivididosDTO {
    private Long id;

    private BigDecimal valorDividido;
    //    ID DO USUARIO
    private String idUsuario;
    //    ID do gasto original
    private Long idGasto;
}