package com.controlegastos.investimentosservice.dto;

import lombok.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestimentoResponseDTO {
    private UUID id;
    private String titulo;
    private Date data;
    private String usuarioId;
    private double precoMedio;
    private double valorTotal;
    private int quantidadeTotal;
}