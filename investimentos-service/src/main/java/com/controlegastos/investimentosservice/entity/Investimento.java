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

    @Temporal(TemporalType.DATE)
    private Date data;

    @OneToMany(mappedBy = "investimento", cascade = CascadeType.ALL)
    private List<Renda> rendas;

    @OneToMany(mappedBy = "investimento", cascade = CascadeType.ALL)
    private List<CompraInvestimento> compras;

}
