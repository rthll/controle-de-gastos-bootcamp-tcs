package com.example.funcionarios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetorDTO {
    private Long id;
    private String nome;
    private String usuarioId;
}
