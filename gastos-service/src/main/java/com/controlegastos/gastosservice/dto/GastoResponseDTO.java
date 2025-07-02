package com.controlegastos.gastosservice.dto;

import lombok.*;

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
    private List<ParcelaDTO> parcelas;
}
