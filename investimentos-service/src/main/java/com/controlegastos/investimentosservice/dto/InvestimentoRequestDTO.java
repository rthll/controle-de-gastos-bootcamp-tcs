package com.controlegastos.investimentosservice.dto;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestimentoRequestDTO {
    private String titulo;
    private Date data;
}