package com.example.categorias_services.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaRequestDTO {

    private String nome;

    private String descricao;

    private BigDecimal limiteDeGasto;

}
