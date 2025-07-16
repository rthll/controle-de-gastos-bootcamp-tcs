package com.example.divisao_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendenteRequestDTO {
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
