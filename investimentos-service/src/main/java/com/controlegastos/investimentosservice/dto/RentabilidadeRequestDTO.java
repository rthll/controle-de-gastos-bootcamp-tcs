package com.controlegastos.investimentosservice.dto;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentabilidadeRequestDTO {
    private UUID investimentoId;
    private double valorAtualPorTitulo;
}