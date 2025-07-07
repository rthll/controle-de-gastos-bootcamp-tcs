package com.example.divisao_service.dto;

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
public class PendenteResponseDTO {

    private UUID id;

    private String descricao;

    private BigDecimal valorTotal;

    private LocalDate data;

    private Integer numeroParcelas;

    //    ID DO USUARIO QUE CRIOU (INICIOU) A DIVISAO
    private String usuarioUmId;
    //    ID DO USUARIO INSERIDO PELO USUARIO INICIAL
    private String usuarioDoisId;

    private String idGasto;

    private String fonte;

    private String categoria;
}
