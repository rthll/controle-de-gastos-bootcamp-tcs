package com.controlegastos.investimentosservice.dto;

import lombok.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraInvestimentoRequestDTO {
    private double valor;
    private int quantidade;
    private Date data;
    private UUID investimentoId;
}