package com.example.categorias_services.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponseDTO {

    private UUID id;

    private String nome;

    private String descricao;

    private String usuarioId;
}
