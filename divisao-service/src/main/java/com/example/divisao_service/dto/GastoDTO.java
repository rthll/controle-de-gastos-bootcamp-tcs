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
public class GastoDTO {

    private UUID id;
    private String descricao;
    private BigDecimal valorTotal;
    private LocalDateTime data;
    private boolean parcelado;
    private Integer numeroParcelas;
    private String usuarioId;
    private String fonte;
    private UUID categoriaId;

}
