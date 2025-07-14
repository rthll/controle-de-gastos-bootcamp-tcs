package com.example.relatorios_service.service;

import com.example.relatorios_service.dto.GastoRelatorioDTO;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DeviceRgb ANT_BALANCE_COLOR = new DeviceRgb(139, 69, 19); // Cor marrom

    public byte[] gerarRelatorioPdf(List<GastoRelatorioDTO> gastos) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            adicionarCabecalho(document);

            adicionarResumo(document, gastos);


            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Erro ao gerar PDF: ", e);
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }
    }

    private void adicionarCabecalho(Document document) throws IOException {
        // Título principal
        Paragraph titulo = new Paragraph("AntBalance - Relatório de Gastos")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ANT_BALANCE_COLOR)
                .setMarginBottom(10);

        document.add(titulo);

        // Linha separadora
        document.add(new LineSeparator(new SolidLine(1f)).setMarginBottom(20));
    }

    private void adicionarResumo(Document document, List<GastoRelatorioDTO> gastos) throws IOException {
        BigDecimal totalGastos = gastos.stream()
                .map(GastoRelatorioDTO::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Cria uma tabela para o resumo
        Table resumoTable = new Table(2);
        resumoTable.setWidth(UnitValue.createPercentValue(50));
        resumoTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

        // Cabeçalho da tabela resumo
        Cell headerCell = new Cell(1, 2)
                .add(new Paragraph("Resumo Geral")
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ANT_BALANCE_COLOR)
                .setTextAlignment(TextAlignment.CENTER);

        resumoTable.addCell(headerCell);

        // Total de gastos
        resumoTable.addCell(new Cell().add(new Paragraph("Total de Gastos:")));
        resumoTable.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", totalGastos))));

        // Quantidade de gastos
        resumoTable.addCell(new Cell().add(new Paragraph("Quantidade de Gastos:")));
        resumoTable.addCell(new Cell().add(new Paragraph(String.valueOf(gastos.size()))));

        // Média de gastos
        BigDecimal media = gastos.isEmpty() ? BigDecimal.ZERO :
                totalGastos.divide(BigDecimal.valueOf(gastos.size()), 2, BigDecimal.ROUND_HALF_UP);
        resumoTable.addCell(new Cell().add(new Paragraph("Média por Gasto:")));
        resumoTable.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", media))));

        document.add(resumoTable);
        document.add(new Paragraph("\n"));
    }

    private void adicionarTabelaDetalhada(Document document, List<GastoRelatorioDTO> gastos) throws IOException {
        // Título da seção
        Paragraph tituloSecao = new Paragraph("Detalhamento dos Gastos")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(16)
                .setMarginBottom(10);

        document.add(tituloSecao);

        // Cria tabela
        Table table = new Table(new float[]{2, 3, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        // Cabeçalhos
        String[] headers = {"Data", "Descrição", "Categoria", "Valor", "Parcela"};
        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header)
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                            .setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ANT_BALANCE_COLOR);
            table.addHeaderCell(cell);
        }

        // Dados
        for (GastoRelatorioDTO gasto : gastos) {
            table.addCell(new Cell().add(new Paragraph(gasto.getData().format(DATE_FORMATTER))));
            table.addCell(new Cell().add(new Paragraph(gasto.getDescricao())));
//            table.addCell(new Cell().add(new Paragraph(gasto.getCategoriaNome())));
            table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", gasto.getValorTotal()))));

//            String parcela = gasto.getParcelado() ?
//                    String.format("%d/%d", gasto.getParcelaAtual(), gasto.getTotalParcelas()) : "-";
//            table.addCell(new Cell().add(new Paragraph(parcela)));
        }

        document.add(table);
    }

    private void adicionarResumoPorCategoria(Document document, List<GastoRelatorioDTO> gastos) throws IOException {
        // Título da seção
        Paragraph tituloSecao = new Paragraph("Resumo por Categoria")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(16)
                .setMarginBottom(10);

        document.add(tituloSecao);

        // Agrupa por categoria
//        Map<String, BigDecimal> gastosPorCategoria = gastos.stream()
//                .collect(Collectors.groupingBy(
//                        GastoRelatorioDTO::getCategoriaNome,
//                        Collectors.mapping(
//                                GastoRelatorioDTO::getValorTotal,
//                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
//                        )
//                ));

        // Cria tabela
        Table table = new Table(new float[]{3, 2, 2});
        table.setWidth(UnitValue.createPercentValue(80));
        table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

        // Cabeçalhos
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Categoria")
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ANT_BALANCE_COLOR));

        table.addHeaderCell(new Cell()
                .add(new Paragraph("Total")
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ANT_BALANCE_COLOR));

        table.addHeaderCell(new Cell()
                .add(new Paragraph("Percentual")
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ANT_BALANCE_COLOR));

        // Total geral para cálculo de percentual
//        BigDecimal totalGeral = gastosPorCategoria.values().stream()
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        // Dados
//        gastosPorCategoria.entrySet().stream()
//                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
//                .forEach(entry -> {
//                    String categoria = entry.getKey();
//                    BigDecimal valor = entry.getValue();
//                    BigDecimal percentual = totalGeral.compareTo(BigDecimal.ZERO) > 0 ?
//                            valor.multiply(BigDecimal.valueOf(100))
//                                    .divide(totalGeral, 2, BigDecimal.ROUND_HALF_UP) :
//                            BigDecimal.ZERO;
//
//                    table.addCell(new Cell().add(new Paragraph(categoria)));
//                    table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", valor))));
//                    table.addCell(new Cell().add(new Paragraph(String.format("%.1f%%", percentual))));
//                });

        document.add(table);
    }
}