package com.example.gastos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesComGastosDTO {
    private String mes; // Ex: Julho/2025
    private List<GastoMesDTO> gastos;
}
