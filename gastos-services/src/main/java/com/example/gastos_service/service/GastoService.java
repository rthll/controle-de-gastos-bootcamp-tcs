package com.example.gastos_service.service;

import com.example.gastos_service.client.CategoriaClient;
import com.example.gastos_service.dto.*;
import com.example.gastos_service.entity.Gasto;
import com.example.gastos_service.entity.Parcela;
import com.example.gastos_service.exception.CategoriaNotFoundException;
import com.example.gastos_service.exception.CategoriaServiceUnavailableException;
import com.example.gastos_service.exception.GastoNotFoundException;
import com.example.gastos_service.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaClient categoriaClient;
    private final TokenService tokenService;

    public List<GastoResponseDTO> listarPorUsuario(String usuarioEmail) {
        return gastoRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<GastoResponseDTO> listarPorUsuarioComFiltroAtivo(String usuarioEmail, Boolean ativo) {
        return gastoRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .filter(gasto -> ativo == null || gasto.isAtivo() == ativo)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<GastoResponseDTO> buscarGastosPorIds(List<Long> gastoIds, String usuarioEmail) {
        if (gastoIds == null || gastoIds.isEmpty()) {
            throw new IllegalArgumentException("Lista de gastos vazia.");
        }

        List<Gasto> gastos = gastoRepository.findAllById(gastoIds);

        if (gastos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum gasto encontrado.");
        }

        List<Gasto> listarApenasUsuario = gastos.stream()
                .filter(gasto -> gasto.getUsuarioId().equals(usuarioEmail))
                .collect(Collectors.toList());
        if (listarApenasUsuario.isEmpty()) {
            throw new IllegalArgumentException("Nenhum gasto para o usuário encontrado.");
        }

        return listarApenasUsuario.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public GastoResponseDTO criarGasto(String usuarioEmail, GastoRequestDTO dto) {
        log.info("Iniciando criação de gasto para usuário: {}", usuarioEmail);

        String token = tokenService.getCurrentToken();

        try {
            if (!categoriaClient.categoriaExiste(dto.getCategoriaId(), token)) {
                log.warn("Categoria {} não encontrada", dto.getCategoriaId());
                throw new CategoriaNotFoundException("Categoria não encontrada");
            }

            log.info("Categoria {} validada com sucesso", dto.getCategoriaId());

        } catch (CategoriaServiceUnavailableException e) {
            log.error("Serviço de categorias indisponível durante criação do gasto: {}", e.getMessage());
            log.warn("Aplicando fallback: criando gasto sem validação de categoria devido à indisponibilidade do serviço");
            // Lançar exceção e não permitir criação
            throw new CategoriaServiceUnavailableException("Não é possível criar o gasto no momento. Serviço de categorias indisponível.");

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

        // Cria as parcelas se necessário
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
        log.info("Gasto criado com sucesso: {}", salvo.getId());

        return mapToResponseDTO(salvo);
    }

    public GastoResponseDTO alterarStatusAtivo(Long gastoId, boolean novoStatus, String emailUsuario) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new GastoNotFoundException("Gasto não encontrado"));

        if (!gasto.getUsuarioId().equals(emailUsuario)) {
            throw new RuntimeException("Você não tem permissão para alterar este gasto.");
        }

        gasto.setAtivo(novoStatus);
        Gasto gastoSalvo = gastoRepository.save(gasto);
        return mapToResponseDTO(gastoSalvo);
    }

    public GastoResponseDTO editarGasto(Long gastoId, GastoRequestDTO dto, String emailUsuario) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new GastoNotFoundException("Gasto não encontrado"));

        if (!gasto.getUsuarioId().equals(emailUsuario)) {
            throw new RuntimeException("Você não tem permissão para editar este gasto.");
        }

        String token = tokenService.getCurrentToken();
        if (!categoriaClient.categoriaExiste(dto.getCategoriaId(), token)) {
            throw new CategoriaNotFoundException("Categoria não encontrada");
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

    public List<MesComGastosDTO> calcularTotalGastosPorMesAgrupado(String usuarioEmail) {
        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuarioEmail);
        Map<YearMonth, List<GastoMesDTO>> mapa = new TreeMap<>(Comparator.reverseOrder());

        for (Gasto gasto : gastos) {
            if (!gasto.isAtivo()) continue;

            CategoriaDTO categoria = categoriaClient.buscarCategoriaPorId(gasto.getCategoriaId(), tokenService.getCurrentToken());

            // Gasto sem parcelas
            if (gasto.getParcelas() == null || gasto.getParcelas().isEmpty()) {
                YearMonth mes = YearMonth.from(gasto.getData());
                mapa.computeIfAbsent(mes, k -> new ArrayList<>())
                        .add(new GastoMesDTO(gasto.getDescricao(), gasto.getValorTotal(), categoria.getNome()));
            } else {
                // Gasto com parcelas
                for (Parcela parcela : gasto.getParcelas()) {
                    YearMonth mes = YearMonth.from(parcela.getDataVencimento());
                    mapa.computeIfAbsent(mes, k -> new ArrayList<>())
                            .add(new GastoMesDTO(gasto.getDescricao(), parcela.getValorParcela(), categoria.getNome()));
                }
            }
        }

        // Monta a lista final
        List<MesComGastosDTO> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR"));

        for (Map.Entry<YearMonth, List<GastoMesDTO>> entry : mapa.entrySet()) {
            String mesFormatado = entry.getKey().format(formatter);
            resultado.add(new MesComGastosDTO(mesFormatado, entry.getValue()));
        }

        return resultado;
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

        CategoriaDTO categoria = null;
        try {
            String token = tokenService.getCurrentToken();
            categoria = categoriaClient.buscarCategoriaPorId(gasto.getCategoriaId(), token);
        } catch (CategoriaServiceUnavailableException e) {
            log.warn("Não foi possível buscar detalhes da categoria {} devido à indisponibilidade do serviço",
                    gasto.getCategoriaId());
        }

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

    public boolean existeGastoComCategoria(Long categoriaId, String usuarioEmail) {
        return gastoRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .anyMatch(gasto -> gasto.getCategoriaId().equals(categoriaId));
    }

    public void excluirGastos(List<Long> gastoIds, String emailUsuario) {
        if (gastoIds == null || gastoIds.isEmpty()) {
            throw new IllegalArgumentException("Lista de IDs não pode estar vazia");
        }

        List<Gasto> gastos = gastoRepository.findAllById(gastoIds);

        if (gastos.size() != gastoIds.size()) {
            throw new GastoNotFoundException("Um ou mais gastos não foram encontrados");
        }

        List<Gasto> gastosNaoAutorizados = gastos.stream()
                .filter(gasto -> !gasto.getUsuarioId().equals(emailUsuario))
                .collect(Collectors.toList());

        if (!gastosNaoAutorizados.isEmpty()) {
            throw new RuntimeException("Você não tem permissão para excluir um ou mais gastos");
        }

        gastoRepository.deleteAll(gastos);
    }

    public void desativarGastos(List<Long> gastoIds, String emailUsuario) {
        if (gastoIds == null || gastoIds.isEmpty()) {
            throw new IllegalArgumentException("Lista de IDs não pode estar vazia");
        }

        List<Gasto> gastos = gastoRepository.findAllById(gastoIds);

        if (gastos.size() != gastoIds.size()) {
            throw new GastoNotFoundException("Um ou mais gastos não foram encontrados");
        }

        List<Gasto> gastosNaoAutorizados = gastos.stream()
                .filter(gasto -> !gasto.getUsuarioId().equals(emailUsuario))
                .collect(Collectors.toList());

        if (!gastosNaoAutorizados.isEmpty()) {
            throw new RuntimeException("Você não tem permissão para desativar um ou mais gastos");
        }

        gastos.forEach(gasto -> gasto.setAtivo(false));
        gastoRepository.saveAll(gastos);
    }

    public GastoResponseDTO buscarPorId(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new GastoNotFoundException("Gasto não encontrada"));

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
                .ativo(true)
                .build();

        Gasto salvo = gastoRepository.save(gasto);
        return mapToResponseDTO(salvo);
    }

    public void deletById(Long id){
        gastoRepository.deleteById(id);
    }

    public void atualizarValor(Long gastoId, BigDecimal valor) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new RuntimeException("Gasto não encontrado"));

        gasto.setValorTotal(valor);
        gastoRepository.save(gasto);
    }

}