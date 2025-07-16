package com.example.funcionarios_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "funcionarios", indexes = {
        @Index(name = "idx_funcionarios_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_funcionarios_setor_id", columnList = "setor_id"),
        @Index(name = "idx_funcionarios_ativo", columnList = "ativo")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String telefone;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "setor_id", nullable = false)
    private Long setorId;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;
}
