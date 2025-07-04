package com.controlegastos.investimentosservice.dto;
import lombok.Data;
import java.util.UUID;

@Data
public class CompraInvestimentoDTO {
    private UUID id;
    private double valor;
    private int quantidade;
    private UUID investimentoId;
}
