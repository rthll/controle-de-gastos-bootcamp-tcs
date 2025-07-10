package com.example.divisao_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DivisaoResponseDTO {
    private GastoDTO gastoUsuarioUm;
    private GastoDTO gastoUsuarioDois;
}
