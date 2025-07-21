package com.example.relatorios_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FuncionarioRelatorioDTO {
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