package com.example.gastos_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "gastos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private boolean ativo;

    private String descricao;

    private BigDecimal valorTotal;

    private LocalDateTime data;

    private boolean parcelado;

    private Integer numeroParcelas;

    private String usuarioId;

    private String fonte;

    private UUID categoriaId;

    @OneToMany(mappedBy = "gasto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcela> parcelas;


}
