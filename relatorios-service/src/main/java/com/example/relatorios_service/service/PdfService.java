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
    private static final DeviceRgb PRIMARY_BLUE = new DeviceRgb(26, 69, 184); // #1a45b8
    private static final DeviceRgb SECONDARY_BLUE = new DeviceRgb(37, 99, 235); // #2563eb
    // #eff6ff
    private static final DeviceRgb GRAY_COLOR = new DeviceRgb(107, 114, 128); // #6b7280
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(243, 244, 246); // #f3f4f6

    public byte[] gerarRelatorioPdf(List<GastoRelatorioDTO> gastos) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            adicionarCabecalho(document);
            adicionarListaDetalhada(document, gastos);
            adicionarResumoGeral(document, gastos);
            adicionarResumoPorCategoria(document, gastos);
            adicionarResumoPorFonte(document, gastos);
            adicionarRodape(document);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Erro ao gerar PDF: ", e);
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }
    }

    private void adicionarCabecalho(Document document) throws IOException {
        Paragraph dataRelatorio = new Paragraph("Gerado em: " + LocalDate.now().format(DATE_FORMATTER))
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontColor(GRAY_COLOR)
                .setMarginBottom(10);

        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1}));
                headerTable.setWidth(UnitValue.createPercentValue(100));
                headerTable.setBackgroundColor(PRIMARY_BLUE);
                headerTable.setMarginBottom(20);

        Paragraph titulo = new Paragraph("AntBalance")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(28)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.WHITE)
                .setMarginTop(20);

        Paragraph subtitulo = new Paragraph("Relatório de Gastos")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.WHITE)
                .setMarginBottom(20);

        Cell headerCell = new Cell()
                .add(titulo)
                .add(subtitulo)
                .setBorder(null);

        document.add(dataRelatorio);
        headerTable.addCell(headerCell);
        document.add(headerTable);

    }

    private void adicionarListaDetalhada(Document document, List<GastoRelatorioDTO> gastos) throws IOException {
        Paragraph tituloSecao = new Paragraph("Lista Detalhada de Gastos")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(PRIMARY_BLUE)
                .setMarginBottom(15);

        document.add(tituloSecao);

        // Tabela de gastos
        Table table = new Table(new float[]{3, 2, 1, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginBottom(30);

        // Cabeçalhos
        String[] headers = {"Descrição", "Valor", "Parcelas", "Categoria", "Fonte", "Data"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(header)
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                            .setFontColor(ColorConstants.WHITE)
                            .setFontSize(11))
                    .setBackgroundColor(PRIMARY_BLUE)
                    .setBorder(null)
                    .setPadding(10);
            table.addHeaderCell(headerCell);
        }

        boolean isOdd = true;
        for (GastoRelatorioDTO gasto : gastos) {
            DeviceRgb bgColor = isOdd ? (DeviceRgb) ColorConstants.WHITE : LIGHT_GRAY;
            System.out.printf(String.valueOf(gasto.getValorTotal()));
            table.addCell(createDataCell(gasto.getDescricao(), bgColor));

            String valor = String.format("R$ %.2f", gasto.getValorTotal());
            table.addCell(createDataCell(valor, bgColor));

            String parcelas = gasto.isParcelado() ?
                    String.format("%dx", gasto.getNumeroParcelas()) : "À vista";
            table.addCell(createDataCell(parcelas, bgColor));

            String categoriaNome = gasto.getCategoria() != null ?
                    gasto.getCategoria().getNome() : "-";
            table.addCell(createDataCell(categoriaNome, bgColor));

            table.addCell(createDataCell(gasto.getFonte(), bgColor));

            String data = gasto.getData().toLocalDate().format(DATE_FORMATTER);
            table.addCell(createDataCell(data, bgColor));

            isOdd = !isOdd;
        }

        document.add(table);
    }

    private Cell createDataCell(String content, DeviceRgb bgColor) {
        try {
            return new Cell()
                    .add(new Paragraph(content != null ? content : "-")
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                            .setFontSize(10))
                    .setBackgroundColor(bgColor)
                    .setBorder(null)
                    .setPadding(8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void adicionarResumoGeral(Document document, List<GastoRelatorioDTO> gastos) throws IOException {
        BigDecimal totalGastos = gastos.stream()
                .map(GastoRelatorioDTO::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal mediaGastos = gastos.isEmpty() ? BigDecimal.ZERO :
                totalGastos.divide(BigDecimal.valueOf(gastos.size()), 2, BigDecimal.ROUND_HALF_UP);

        // Container do resumo
        Table resumoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}));
        resumoTable.setWidth(UnitValue.createPercentValue(100));
        resumoTable.setMarginBottom(30);

        // Cards de resumo
        Cell totalCard = createSummaryCard("Total de Gastos",
                String.format("R$ %.2f", totalGastos), SECONDARY_BLUE);
        Cell quantidadeCard = createSummaryCard("Quantidade",
                String.valueOf(gastos.size()), PRIMARY_BLUE);
        Cell mediaCard = createSummaryCard("Média por Gasto",
                String.format("R$ %.2f", mediaGastos), PRIMARY_BLUE);

        resumoTable.addCell(quantidadeCard);
        resumoTable.addCell(totalCard);
        resumoTable.addCell(mediaCard);

        document.add(resumoTable);
    }

    private Cell createSummaryCard(String label, String value, DeviceRgb color) throws IOException {
        Table cardTable = new Table(1);
        cardTable.setWidth(UnitValue.createPercentValue(100));

        Paragraph labelPara = new Paragraph(label)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(10)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph valuePara = new Paragraph(value)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);

        Cell content = new Cell()
                .add(labelPara)
                .add(valuePara)
                .setBackgroundColor(color)
                .setBorder(null)
                .setPadding(15);

        cardTable.addCell(content);

        return new Cell()
                .add(cardTable)
                .setBorder(null)
                .setPadding(5);
    }

    private void adicionarResumoPorCategoria(Document document, List<GastoRelatorioDTO> gastos) throws IOException {
        Paragraph tituloSecaoCategoria = new Paragraph("Resumo por Categoria")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(PRIMARY_BLUE)
                .setMarginBottom(15);

        document.add(tituloSecaoCategoria);

        Map<String, BigDecimal> gastosPorCategoria = gastos.stream()
                .collect(Collectors.groupingBy(
                        gasto -> gasto.getCategoria() != null ?
                                gasto.getCategoria().getNome() : "Sem categoria",
                        Collectors.mapping(
                                GastoRelatorioDTO::getValorTotal,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        BigDecimal totalGeralCategorias = gastosPorCategoria.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tabela de categorias
        Table tableCategorias = new Table(new float[]{3, 2, 2});
        tableCategorias.setWidth(UnitValue.createPercentValue(100));

        // Cabeçalhos
        tableCategorias.addHeaderCell(createHeaderCell("Categoria"));
        tableCategorias.addHeaderCell(createHeaderCell("Total"));
        tableCategorias.addHeaderCell(createHeaderCell("Percentual"));

        // Dados ordenados por valor
        gastosPorCategoria.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .forEach(entry -> {
                    String categoria = entry.getKey();
                    BigDecimal valor = entry.getValue();
                    BigDecimal percentual = totalGeralCategorias.compareTo(BigDecimal.ZERO) > 0 ?
                            valor.multiply(BigDecimal.valueOf(100))
                                    .divide(totalGeralCategorias, 2, BigDecimal.ROUND_HALF_UP) :
                            BigDecimal.ZERO;

                    try {
                        tableCategorias.addCell(createDataCell(categoria, (DeviceRgb) ColorConstants.WHITE));
                        tableCategorias.addCell(createDataCell(String.format("R$ %.2f", valor), (DeviceRgb) ColorConstants.WHITE));
                        tableCategorias.addCell(createDataCell(String.format("%.1f%%", percentual), (DeviceRgb) ColorConstants.WHITE));
                    } catch (Exception e) {
                        log.error("Erro ao adicionar linha da categoria", e);
                    }
                });

        document.add(tableCategorias);
        document.add(new LineSeparator(new SolidLine(1f))
                .setMarginTop(30)
                .setMarginBottom(10));
    }

    private void adicionarResumoPorFonte(Document document, List<GastoRelatorioDTO> gastos) throws IOException {
        Paragraph tituloSecaoFonte = new Paragraph("Resumo por Fonte")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(PRIMARY_BLUE)
                .setMarginBottom(15);

        document.add(tituloSecaoFonte);

        Map<String, BigDecimal> gastosPorFonte = gastos.stream()
                .collect(Collectors.groupingBy(
                        gasto -> gasto.getFonte() != null ?
                                gasto.getFonte() : "Sem fonte",
                        Collectors.mapping(
                                GastoRelatorioDTO::getValorTotal,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        BigDecimal totalGeralFontes= gastosPorFonte.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tabela de categorias
        Table tableFontes = new Table(new float[]{3, 2, 2});
        tableFontes.setWidth(UnitValue.createPercentValue(100));

        // Cabeçalhos
        tableFontes.addHeaderCell(createHeaderCell("Fonte"));
        tableFontes.addHeaderCell(createHeaderCell("Total"));
        tableFontes.addHeaderCell(createHeaderCell("Percentual"));

        // Dados ordenados por valor
        gastosPorFonte.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .forEach(entry -> {
                    String categoria = entry.getKey();
                    BigDecimal valor = entry.getValue();
                    BigDecimal percentual = totalGeralFontes.compareTo(BigDecimal.ZERO) > 0 ?
                            valor.multiply(BigDecimal.valueOf(100))
                                    .divide(totalGeralFontes, 2, BigDecimal.ROUND_HALF_UP) :
                            BigDecimal.ZERO;

                    try {
                        tableFontes.addCell(createDataCell(categoria, (DeviceRgb) ColorConstants.WHITE));
                        tableFontes.addCell(createDataCell(String.format("R$ %.2f", valor), (DeviceRgb) ColorConstants.WHITE));
                        tableFontes.addCell(createDataCell(String.format("%.1f%%", percentual), (DeviceRgb) ColorConstants.WHITE));
                    } catch (Exception e) {
                        log.error("Erro ao adicionar linha da categoria", e);
                    }
                });

        document.add(tableFontes);
    }

    private Cell createHeaderCell(String content) throws IOException {
        return new Cell()
                .add(new Paragraph(content)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontColor(ColorConstants.WHITE)
                        .setFontSize(11))
                .setBackgroundColor(PRIMARY_BLUE)
                .setBorder(null)
                .setPadding(10);
    }

    private void adicionarRodape(Document document) throws IOException {
        // Linha separadora
        document.add(new LineSeparator(new SolidLine(1f))
                .setMarginTop(30)
                .setMarginBottom(10));

        // Rodapé
        Paragraph rodape = new Paragraph("© 2025 AntBalance - Sistema de Controle Financeiro")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(GRAY_COLOR);

        document.add(rodape);
    }
}