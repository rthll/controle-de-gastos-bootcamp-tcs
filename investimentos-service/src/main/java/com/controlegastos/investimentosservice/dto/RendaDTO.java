package com.controlegastos.investimentosservice.dto;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class RendaDTO {
    private UUID id;

    private String titulo;

    private double valor;

    private Date data;

    private UUID investimentoId;
}
