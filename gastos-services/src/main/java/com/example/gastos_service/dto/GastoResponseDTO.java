package com.example.gastos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoResponseDTO {

    private UUID id;
    private boolean ativo;
    private String descricao;
    private BigDecimal valorTotal;
    private LocalDateTime data;
    private boolean parcelado;
    private Integer numeroParcelas;
    private String usuarioId;
    private String fonte;
    private UUID categoriaId;
    private CategoriaDTO categoria;

    private List<ParcelaDTO> parcelas;
}