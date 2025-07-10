package com.example.gastos_service.entity;

import com.example.gastos_service.entity.Gasto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parcelas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer numeroParcela;

    private BigDecimal valorParcela;

    private LocalDateTime dataVencimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gasto_id")
    @ToString.Exclude
    private Gasto gasto;
}
