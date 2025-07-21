package com.example.divisao_service.dto;

import com.example.divisao_service.entity.GastosDivididos;
import com.example.divisao_service.entity.Pendente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListDivididosResponseDTO {
    private List<Pendente> pendentes ;
    private List<GastosDivididos> aceitos;
}