package com.example.divisao_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pendente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valorTotal;

    private BigDecimal valorDividido;

    private LocalDateTime data;

    private String usuarioUmId;

    private String usuarioDoisId;

    private Long idGasto;

    private String fonte;

    private Long categoriaId;
}
