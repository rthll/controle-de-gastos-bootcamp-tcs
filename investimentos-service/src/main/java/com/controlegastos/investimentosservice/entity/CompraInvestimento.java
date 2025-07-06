package com.controlegastos.investimentosservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "CompraInvestimentos")
public class CompraInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private double valor;
    private int quantidade;

    @Temporal(TemporalType.DATE)
    private Date data;

    private String usuarioId;

    @ManyToOne
    @JoinColumn(name = "investimento_id")
    private Investimento investimento;
}