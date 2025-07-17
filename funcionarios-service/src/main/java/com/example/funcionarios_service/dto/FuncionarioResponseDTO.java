package com.example.funcionarios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioResponseDTO {
    private Long id;
    private String nome;
    private String cargo;
    private String telefone;
    private LocalDateTime dataCadastro;
    private BigDecimal salario;
    private boolean ativo;
    private String usuarioId;
    private SetorDTO setor;
}
