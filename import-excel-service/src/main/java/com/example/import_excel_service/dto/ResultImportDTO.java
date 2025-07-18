package com.example.import_excel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResultImportDTO {

    private Integer totalGastosImportados;
    private List<GastoImportDTO> gastosImportados;

}
