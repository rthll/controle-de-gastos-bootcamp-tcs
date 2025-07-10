package com.example.divisao_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pendente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pendente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String descricao;

    private BigDecimal valorTotal;

    private BigDecimal valorDividido;

    private LocalDate data;

//    ID DO USUARIO QUE CRIOU (INICIOU) A DIVISAO
    private String usuarioUmId;
//    ID DO USUARIO INSERIDO PELO USUARIO INICIAL
    private String usuarioDoisId;
//    Necessario o idGasto para saber qual gasto excluir ao ser confirmado
    private UUID idGasto;

    private String fonte;

    private UUID categoriaId;
}
