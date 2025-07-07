package com.example.categorias_services.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaRequestDTO {

    private String nome;

    private String descricao;

}
