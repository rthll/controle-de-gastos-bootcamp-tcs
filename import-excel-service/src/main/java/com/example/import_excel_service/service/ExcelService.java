package com.example.import_excel_service.service;

import com.example.import_excel_service.dto.GastoRequestDTO;
import com.example.import_excel_service.dto.ImportExcelDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

public List<ImportExcelDTO> extract (MultipartFile file) {
        List<ImportExcelDTO> importExcelDTOList = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex < sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row != null) {
                    Gasto gasto = Gasto.builder()
                    .descricao(dto.getDescricao())
                    .valorTotal(dto.getValorTotal())
                    .data(dto.getData())
                    .parcelado(dto.isParcelado())
                    .numeroParcelas(dto.getNumeroParcelas())
                    .usuarioId(usuarioEmail)
                    .fonte(dto.getFonte())
                    .categoriaId(dto.getCategoriaId())
                    .ativo(true)
                    .build();

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

}

}
