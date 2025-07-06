package com.controlegastos.investimentosservice.dto;

import lombok.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendaRequestDTO {
    private String titulo;
    private double valor;
    private Date data;
    private UUID investimentoId;
}