package com.controlegastos.investimentosservice.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestimentoRequestDTO {
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 100, message = "Título deve ter entre 2 e 100 caracteres")
    private String titulo;

    @NotNull(message = "Data é obrigatória")
    @PastOrPresent(message = "Data não pode ser futura")
    private Date data;

    @NotBlank(message = "Tipo é obrigatório")
    @Size(min = 2, max = 50, message = "Tipo deve ter entre 2 e 50 caracteres")
    private String tipo;

    @DecimalMin(value = "0.0", inclusive = false, message = "Taxa de rendimento deve ser maior que 0")
    @DecimalMax(value = "1.0", message = "Taxa de rendimento deve ser menor ou igual a 1 (100%)")
    private double taxaRendimentoMensal;
}