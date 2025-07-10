package com.example.divisao_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendenteRequestDTO {
    private UUID id;

    private String descricao;

    private BigDecimal valorTotal;

    private BigDecimal valorDividido;

    private LocalDateTime data;

    //    ID DO USUARIO QUE CRIOU (INICIOU) A DIVISAO
    private String usuarioUmId;
    //    ID DO USUARIO INSERIDO PELO USUARIO INICIAL
    private String usuarioDoisId;

    private UUID idGasto;

    private String fonte;

    private UUID categoriaId;
}
