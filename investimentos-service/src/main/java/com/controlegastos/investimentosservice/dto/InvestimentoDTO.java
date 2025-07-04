package com.controlegastos.investimentosservice.dto;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class InvestimentoDTO {
    private UUID id;

    private String titulo;

    private Date data;

    private String usuarioId;

    private double precoMedio;

    private double valorTotal;

    private double rentabilidade;
}
