package com.controlegastos.investimentosservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "investimentos")
public class Investimento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String titulo;
    private String tipo;
    private double taxaRendimentoMensal;

    @Temporal(TemporalType.DATE)
    private Date data;
    private String usuarioId;

    @OneToMany(mappedBy = "investimento", cascade = CascadeType.ALL)
    private List<Renda> rendas;

    @OneToMany(mappedBy = "investimento", cascade = CascadeType.ALL)
    private List<CompraInvestimento> compras;
}