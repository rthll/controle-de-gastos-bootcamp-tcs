package com.example.gastos_service.dto;

import com.example.gastos_service.dto.ParcelaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoResponseDTO {

    private UUID id;

    private String descricao;

    private BigDecimal valorTotal;

    private LocalDate data;

    private boolean parcelado;

    private Integer numeroParcelas;

    private String usuarioId;

    private String fonte;

    private String categoria;

    private List<ParcelaDTO> parcelas;

}
