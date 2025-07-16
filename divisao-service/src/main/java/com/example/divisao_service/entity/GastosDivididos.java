package com.example.divisao_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "gastos_divididos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastosDivididos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valorDividido;
    //    ID DO USUARIO
    private String idUsuario;
    //    ID do gasto original
    private Long idGasto;
}
