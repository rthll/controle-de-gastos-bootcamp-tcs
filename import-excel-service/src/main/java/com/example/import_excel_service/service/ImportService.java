package com.example.import_excel_service.service;

import com.example.import_excel_service.client.CategoriaClient;
import com.example.import_excel_service.client.GastoClient;
import com.example.import_excel_service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {

    private final GastoClient gastoClient;
    private final CategoriaClient categoriaClient;

    public ResultImportDTO importarGastos(MultipartFile file, String usuarioId, String token) throws IOException {
        List<GastoImportDTO> gastosImportados = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                try {
                    String descricao = getCellValueAsString(row.getCell(0));
                    BigDecimal valorTotal = getCellValueAsBigDecimal(row.getCell(1));
                    LocalDateTime data = getCellValueAsLocalDateTime(row.getCell(2));
                    Integer numeroParcelas = getCellValueAsInteger(row.getCell(3));
                    String fonte = getCellValueAsString(row.getCell(4));
                    String nomeCategoria = getCellValueAsString(row.getCell(5));

                    //método de criar categoria

                    GastoRequestDTO gastoRequest = GastoRequestDTO.builder()
                            .descricao(descricao)
                            .valorTotal(valorTotal)
                            .data(data)
                            .parcelado(true)
                            .numeroParcelas(numeroParcelas)
                            .fonte(fonte)
                            .categoriaId(categoria.getId())
                            .usuarioId(usuarioId)
                            .build();

                    List<GastoImportDTO> gastoCriado = gastoClient.criarGasto(gastoRequest, token);

                    if (gastoCriado != null && !gastoCriado.isEmpty()) {
                        gastosImportados.addAll(gastoCriado);
                    }
                } catch (Exception e) {
                    log.error("Erro ao processar linha {}: {}", row.getRowNum() + 1, e.getMessage());
                    }
             }
        } catch (Exception e) {
            log.error("Erro no arquivo Excel: {}", e.getMessage());
        }
        return ResultImportDTO.builder()
                .totalGastosImportados(gastosImportados.size())
                .gastosImportados(gastosImportados)
                .build();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }

        throw new IllegalArgumentException("Valor inválido.");
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return 1;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }

        throw new IllegalArgumentException("Número de parcelas inválido");
    }

    private LocalDateTime getCellValueAsLocalDateTime(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Data inválida.");
        }

    }


}
