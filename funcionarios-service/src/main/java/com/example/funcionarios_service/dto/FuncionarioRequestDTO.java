package com.example.funcionarios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioRequestDTO {
    private String nome;
    private String cargo;
    private String telefone;
    private LocalDateTime dataCadastro;
    private BigDecimal salario;
    private boolean ativo;
    private String usuarioId;
    private Long setorId;
}
