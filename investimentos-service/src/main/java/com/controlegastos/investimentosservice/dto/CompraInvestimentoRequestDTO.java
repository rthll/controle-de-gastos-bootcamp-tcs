package com.controlegastos.investimentosservice.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraInvestimentoRequestDTO {
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que 0")
    private double valor;

    @NotNull(message = "Data é obrigatória")
    @PastOrPresent(message = "Data não pode ser futura")
    private Date data;

    @NotNull(message = "ID do investimento é obrigatório")
    private UUID investimentoId;
}
