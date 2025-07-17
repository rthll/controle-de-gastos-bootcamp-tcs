package com.example.categorias_services.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponseDTO {

    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal limiteDeGasto;

    private String usuarioId;
}
