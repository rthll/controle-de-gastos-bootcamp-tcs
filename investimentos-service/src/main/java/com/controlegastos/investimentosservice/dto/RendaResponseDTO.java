package com.controlegastos.investimentosservice.dto;

import lombok.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendaResponseDTO {
    private UUID id;
    private String titulo;
    private double valor;
    private Date data;
    private String usuarioId;
    private UUID investimentoId;
}
