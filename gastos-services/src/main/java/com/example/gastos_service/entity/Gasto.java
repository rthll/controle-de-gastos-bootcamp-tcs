package com.example.gastos_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "gastos", indexes = {
        @Index(name = "idx_gastos_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_gastos_categoria_id", columnList = "categoria_id"),
        @Index(name = "idx_gastos_ativo", columnList = "ativo")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "valor_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private boolean parcelado;

    @Column(name = "numero_parcelas")
    private Integer numeroParcelas;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    private String fonte;

    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @OneToMany(mappedBy = "gasto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcela> parcelas;
}