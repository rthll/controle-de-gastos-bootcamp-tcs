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
    private final TokenService tokenService;

    public GastoResponseDTO criarGasto(String usuarioEmail, GastoRequestDTO dto) {
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
                .ativo(true)
                .build();

        if (dto.isParcelado() && dto.getNumeroParcelas() != null && dto.getNumeroParcelas() > 1) {
            List<Parcela> parcelas = new ArrayList<>();
            BigDecimal valorParcela = dto.getValorTotal()
                    .divide(BigDecimal.valueOf(dto.getNumeroParcelas()), 2, RoundingMode.HALF_UP);

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

    public GastoResponseDTO alterarStatusAtivo(UUID gastoId, boolean novoStatus, String emailUsuario) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new RuntimeException("Gasto não encontrado"));

        if (!gasto.getUsuarioId().equals(emailUsuario)) {
            throw new RuntimeException("Você não tem permissão para alterar este gasto.");
        }

        gasto.setAtivo(novoStatus);
        Gasto gastoSalvo = gastoRepository.save(gasto);
        return mapToResponseDTO(gastoSalvo);
    }

    public GastoResponseDTO editarGasto(UUID gastoId, GastoRequestDTO dto, String emailUsuario) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new RuntimeException("Gasto não encontrado"));

        if (!gasto.getUsuarioId().equals(emailUsuario)) {
            throw new RuntimeException("Você não tem permissão para editar este gasto.");
        }

        String token = tokenService.getCurrentToken();
        if (!categoriaClient.categoriaExiste(dto.getCategoriaId(), token)) {
            throw new RuntimeException("Categoria não encontrada");
        }

        gasto.setDescricao(dto.getDescricao());
        gasto.setValorTotal(dto.getValorTotal());
        gasto.setData(dto.getData());
        gasto.setFonte(dto.getFonte());
        gasto.setCategoriaId(dto.getCategoriaId());

        if (dto.isParcelado() != gasto.isParcelado() ||
                !Objects.equals(dto.getNumeroParcelas(), gasto.getNumeroParcelas())) {

            if (gasto.getParcelas() != null) {
                gasto.getParcelas().clear();
            }

            gasto.setParcelado(dto.isParcelado());
            gasto.setNumeroParcelas(dto.getNumeroParcelas());

            if (dto.isParcelado() && dto.getNumeroParcelas() != null && dto.getNumeroParcelas() > 1) {
                List<Parcela> novasParcelas = new ArrayList<>();
                BigDecimal valorParcela = dto.getValorTotal()
                        .divide(BigDecimal.valueOf(dto.getNumeroParcelas()), 2, RoundingMode.HALF_UP);

                for (int i = 0; i < dto.getNumeroParcelas(); i++) {
                    Parcela parcela = Parcela.builder()
                            .numeroParcela(i + 1)
                            .valorParcela(valorParcela)
                            .dataVencimento(dto.getData().plusMonths(i))
                            .gasto(gasto)
                            .build();
                    novasParcelas.add(parcela);
                }
                gasto.setParcelas(novasParcelas);
            }
        }

        Gasto gastoSalvo = gastoRepository.save(gasto);
        return mapToResponseDTO(gastoSalvo);
    }

    public List<GastoResponseDTO> listarPorUsuario(String usuarioEmail) {
        return gastoRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal calcularTotalGastosMesAtual(String usuarioEmail) {
        YearMonth mesAtual = YearMonth.now();

        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuarioEmail);

        BigDecimal total = gastos.stream()
                .filter(Gasto::isAtivo)
                .peek(gasto -> System.out.println("Gasto: " + gasto.getDescricao() + ", Parcelado: " + gasto.isParcelado()))
                .flatMap(gasto -> {
                    if (gasto.getParcelas() != null) {
                        return gasto.getParcelas().stream();
                    } else {
                        return Stream.empty();
                    }
                })
                .peek(parcela -> {
                    YearMonth vencimento = YearMonth.from(parcela.getDataVencimento());
                })
                .filter(parcela -> {
                    YearMonth vencimento = YearMonth.from(parcela.getDataVencimento());
                    return vencimento.equals(mesAtual);
                })
                .map(Parcela::getValorParcela)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total;
    }

    public BigDecimal calcularTotalGastosFuturos(String usuarioEmail) {
        LocalDate hoje = LocalDate.now();

        return gastoRepository.findByUsuarioId(usuarioEmail).stream()
                .filter(Gasto::isAtivo)
                .flatMap(gasto -> {
                    if (gasto.getParcelas() != null && !gasto.getParcelas().isEmpty()) {
                        return gasto.getParcelas().stream();
                    } else {
                        return Stream.empty();
                    }
                })
                .filter(parcela -> parcela.getDataVencimento().toLocalDate().isAfter(hoje))
                .map(Parcela::getValorParcela)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<YearMonth, BigDecimal> calcularTotalGastosPorMes(String usuarioEmail) {
        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuarioEmail);
        Map<YearMonth, BigDecimal> totalPorMes = new TreeMap<>();

        gastos.stream()
                .filter(Gasto::isAtivo)
                .forEach(gasto -> {
                    if (gasto.isParcelado() && gasto.getParcelas() != null && !gasto.getParcelas().isEmpty()) {
                        gasto.getParcelas().forEach(parcela -> {
                            YearMonth mesVencimento = YearMonth.from(parcela.getDataVencimento());
                            totalPorMes.merge(mesVencimento, parcela.getValorParcela(), BigDecimal::add);
                        });
                    } else {
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
                .ativo(gasto.isAtivo())
                .build();
    }

    public boolean existeGastoComCategoria(UUID categoriaId, String usuarioEmail) {
        return gastoRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .filter(Gasto::isAtivo)
                .anyMatch(gasto -> gasto.getCategoriaId().equals(categoriaId));
    }
}