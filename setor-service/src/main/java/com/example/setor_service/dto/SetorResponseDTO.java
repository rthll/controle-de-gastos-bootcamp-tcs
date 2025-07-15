package com.example.setor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetorResponseDTO {
    private Long id;

    private String nome;

    private String usuarioId;

}
