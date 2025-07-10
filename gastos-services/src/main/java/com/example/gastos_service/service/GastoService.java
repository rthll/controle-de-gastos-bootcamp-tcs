package com.example.gastos_service.service;

import com.example.gastos_service.client.CategoriaClient;
import com.example.gastos_service.dto.CategoriaDTO;
import com.example.gastos_service.dto.GastoRequestDTO;
import com.example.gastos_service.dto.GastoResponseDTO;
import com.example.gastos_service.dto.ParcelaDTO;
import com.example.gastos_service.entity.Gasto;
import com.example.gastos_service.entity.Parcela;
import com.example.gastos_service.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaClient categoriaClient;
    private final TokenService tokenService; // Serviço para gerenciar tokens

    public GastoResponseDTO criarGasto(String usuarioEmail, GastoRequestDTO dto) {
        // Validar se a categoria existe
        String token = tokenService.getCurrentToken();
        if (!categoriaClient.categoriaExiste(dto.getCategoriaId(), token)) {
            throw new RuntimeException("Categoria não encontrada");
        }

        Gasto gasto = Gasto.builder()
                .descricao(dto.getDescricao())
                .valorTotal(dto.getValorTotal())
                .data(dto.getData())
                .parcelado(dto.isParcelado())
                .numeroParcelas(dto.getNumeroParcelas())
                .usuarioId(usuarioEmail)
                .fonte(dto.getFonte())
                .categoriaId(dto.getCategoriaId())
                .build();

        // Se for parcelado, cria parcelas
        if (dto.isParcelado() && dto.getNumeroParcelas() != null && dto.getNumeroParcelas() > 1) {
            List<Parcela> parcelas = new ArrayList<>();
            BigDecimal valorParcela = dto.getValorTotal()
                    .divide(BigDecimal.valueOf(dto.getNumeroParcelas()), 2, RoundingMode.HALF_UP); //método que foi substituido estava deprecated

            for (int i = 0; i < dto.getNumeroParcelas(); i++) {
                Parcela parcela = Parcela.builder()
                        .numeroParcela(i + 1)
                        .valorParcela(valorParcela)
                        .dataVencimento(dto.getData().plusMonths(i))
                        .gasto(gasto)
                        .build();
                parcelas.add(parcela);
            }

            gasto.setParcelas(parcelas);
        }

        Gasto salvo = gastoRepository.save(gasto);
        return mapToResponseDTO(salvo);
    }

    public List<GastoResponseDTO> listarPorUsuario(String usuarioEmail) {
        return gastoRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal calcularTotalGastosMesAtual(String usuarioEmail) {
        YearMonth mesAtual = YearMonth.now();

        // Debug: Verificar se há gastos para o usuário
        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuarioEmail);
        System.out.println("Gastos encontrados para o usuário: " + gastos.size());

        BigDecimal total = gastos.stream()
                .peek(gasto -> System.out.println("Gasto: " + gasto.getDescricao() + ", Parcelado: " + gasto.isParcelado()))
                .flatMap(gasto -> {
                    // Debug: Verificar se tem parcelas
                    if (gasto.getParcelas() != null) {
                        System.out.println("Parcelas do gasto '" + gasto.getDescricao() + "': " + gasto.getParcelas().size());
                        return gasto.getParcelas().stream();
                    } else {
                        System.out.println("Gasto '" + gasto.getDescricao() + "' não tem parcelas");
                        return Stream.empty();
                    }
                })
                .peek(parcela -> {
                    YearMonth vencimento = YearMonth.from(parcela.getDataVencimento());
                    System.out.println("Parcela: " + parcela.getNumeroParcela() +
                            ", Vencimento: " + parcela.getDataVencimento() +
                            ", Mês/Ano: " + vencimento +
                            ", Mês atual: " + mesAtual +
                            ", Corresponde ao mês atual? " + vencimento.equals(mesAtual));
                })
                .filter(parcela -> {
                    YearMonth vencimento = YearMonth.from(parcela.getDataVencimento());
                    return vencimento.equals(mesAtual);
                })
                .map(Parcela::getValorParcela)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Total calculado: " + total);
        return total;
    }

    public BigDecimal calcularTotalGastosFuturos(String usuarioEmail) {
        LocalDate hoje = LocalDate.now();

        return gastoRepository.findByUsuarioId(usuarioEmail).stream()
                .flatMap(gasto -> gasto.getParcelas().stream())
                .filter(parcela -> parcela.getDataVencimento().isAfter(hoje))
                .map(Parcela::getValorParcela)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<YearMonth, BigDecimal> calcularTotalGastosPorMes(String usuarioEmail) {
        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuarioEmail);

        Map<YearMonth, BigDecimal> totalPorMes = new TreeMap<>();

        gastos.forEach(gasto -> {
            if (gasto.isParcelado() && gasto.getParcelas() != null && !gasto.getParcelas().isEmpty()) {
                // Para gastos parcelados, distribui as parcelas pelos meses
                gasto.getParcelas().forEach(parcela -> {
                    YearMonth mesVencimento = YearMonth.from(parcela.getDataVencimento());
                    totalPorMes.merge(mesVencimento, parcela.getValorParcela(), BigDecimal::add);
                });
            } else {
                // Para gastos não parcelados, adiciona no mês do gasto
                YearMonth mesGasto = YearMonth.from(gasto.getData());
                totalPorMes.merge(mesGasto, gasto.getValorTotal(), BigDecimal::add);
            }
        });

        return totalPorMes;
    }

    private GastoResponseDTO mapToResponseDTO(Gasto gasto) {
        List<ParcelaDTO> parcelas = new ArrayList<>();
        if (gasto.isParcelado() && gasto.getParcelas() != null) {
            parcelas = gasto.getParcelas().stream().map(p -> ParcelaDTO.builder()
                    .id(p.getId())
                    .numeroParcela(p.getNumeroParcela())
                    .valorParcela(p.getValorParcela())
                    .dataVencimento(p.getDataVencimento())
                    .build()).collect(Collectors.toList());
        }

        String token = tokenService.getCurrentToken();
        CategoriaDTO categoria = categoriaClient.buscarCategoriaPorId(gasto.getCategoriaId(), token);

        return GastoResponseDTO.builder()
                .id(gasto.getId())
                .descricao(gasto.getDescricao())
                .valorTotal(gasto.getValorTotal())
                .data(gasto.getData())
                .parcelado(gasto.isParcelado())
                .numeroParcelas(gasto.getNumeroParcelas())
                .usuarioId(gasto.getUsuarioId())
                .fonte(gasto.getFonte())
                .categoriaId(gasto.getCategoriaId())
                .categoria(categoria)
                .parcelas(parcelas)
                .build();
    }

    public GastoResponseDTO buscarPorId(UUID id, String usuarioEmail) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto não encontrada"));

        if (!gasto.getUsuarioId().equals(usuarioEmail)) {
            throw new RuntimeException("Gasto não pertence ao usuário");
        }

        return toResponseDTO(gasto);
    }

    private GastoResponseDTO toResponseDTO(Gasto gasto) {
        return GastoResponseDTO.builder()
                .id(gasto.getId())
                .descricao(gasto.getDescricao())
                .valorTotal(gasto.getValorTotal())
                .data(gasto.getData())
                .parcelado(gasto.isParcelado())
                .numeroParcelas(gasto.getNumeroParcelas())
                .usuarioId(gasto.getUsuarioId())
                .fonte(gasto.getFonte())
                .categoriaId(gasto.getCategoriaId())
                .build();
    }

    public GastoResponseDTO criarGastoDivisao(GastoRequestDTO dto) {
        Gasto gasto = Gasto.builder()
                .descricao(dto.getDescricao())
                .valorTotal(dto.getValorTotal())
                .data(dto.getData())
                .parcelado(dto.isParcelado())
                .numeroParcelas(dto.getNumeroParcelas())
                .usuarioId(dto.getUsuarioId())
                .fonte(dto.getFonte())
                .categoriaId(dto.getCategoriaId())
                .build();

        Gasto salvo = gastoRepository.save(gasto);
        return mapToResponseDTO(salvo);
    }

    public void deletById(UUID id){
        gastoRepository.deleteById(id);
    }
}