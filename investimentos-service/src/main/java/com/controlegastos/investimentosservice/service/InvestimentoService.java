package com.controlegastos.investimentosservice.service;

import com.controlegastos.investimentosservice.dto.InvestimentoRequestDTO;
import com.controlegastos.investimentosservice.dto.InvestimentoResponseDTO;
import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import com.controlegastos.investimentosservice.entity.Investimento;
import com.controlegastos.investimentosservice.entity.Renda;
import com.controlegastos.investimentosservice.exception.InvestimentoNotFoundException;
import com.controlegastos.investimentosservice.repository.InvestimentoRepository;
import com.controlegastos.investimentosservice.repository.RendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestimentoService {
    private final InvestimentoRepository repository;
    private final RendaRepository rendaRepository;

    public InvestimentoResponseDTO criarInvestimento(String usuarioEmail, InvestimentoRequestDTO dto) {
        Investimento investimento = Investimento.builder()
                .titulo(dto.getTitulo())
                .tipo(dto.getTipo())
                .taxaRendimentoMensal(dto.getTaxaRendimentoMensal())
                .data(dto.getData())
                .usuarioId(usuarioEmail)
                .build();

        investimento = repository.save(investimento);
        return toResponseDTO(investimento);
    }

    public List<InvestimentoResponseDTO> listarInvestimentosPorUsuario(String usuarioEmail) {
        return repository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public InvestimentoResponseDTO buscarPorId(UUID id, String usuarioEmail) {
        Investimento investimento = repository.findByIdAndUsuarioId(id, usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));
        return toResponseDTO(investimento);
    }

    public void deletar(UUID id, String usuarioEmail) {
        Investimento investimento = repository.findByIdAndUsuarioId(id, usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));
        repository.deleteById(id);
    }

    public double calcularValorTotal(UUID investimentoId, String usuarioEmail) {
        Investimento inv = repository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));

        if (inv.getCompras() == null || inv.getCompras().isEmpty()) {
            return 0.0;
        }

        return inv.getCompras().stream()
                .mapToDouble(CompraInvestimento::getValor)
                .sum();
    }

    public double calcularValorAtualTotal(UUID investimentoId, String usuarioEmail) {
        double valorInicial = calcularValorTotal(investimentoId, usuarioEmail);
        double totalRendas = calcularTotalRendas(investimentoId, usuarioEmail);
        return valorInicial + totalRendas;
    }

    public double calcularTotalRendas(UUID investimentoId, String usuarioEmail) {
        Investimento inv = repository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));

        if (inv.getRendas() == null || inv.getRendas().isEmpty()) {
            return 0.0;
        }

        return inv.getRendas().stream()
                .mapToDouble(Renda::getValor)
                .sum();
    }

    public double calcularRendimentoAcumulado(UUID investimentoId, String usuarioEmail) {
        return calcularTotalRendas(investimentoId, usuarioEmail);
    }

    public double calcularRentabilidade(UUID investimentoId, String usuarioEmail) {
        double valorInvestido = calcularValorTotal(investimentoId, usuarioEmail);

        if (valorInvestido == 0) {
            return 0.0;
        }

        double rendimentoAcumulado = calcularRendimentoAcumulado(investimentoId, usuarioEmail);
        return (rendimentoAcumulado / valorInvestido) * 100;
    }

    public double calcularRendimentoMensal(UUID investimentoId, String usuarioEmail) {
        double valorTotal = calcularValorTotal(investimentoId, usuarioEmail);
        double totalRendas = calcularTotalRendas(investimentoId, usuarioEmail);
        double valorAtual = valorTotal + totalRendas;

        Investimento inv = repository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));

        return valorAtual * inv.getTaxaRendimentoMensal();
    }

    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void gerarRendaMensal() {
        List<Investimento> todosInvestimentos = repository.findAll();

        for (Investimento investimento : todosInvestimentos) {
            gerarRendaMensalParaInvestimento(investimento);
        }
    }

    @Transactional
    public void gerarRendaMensalParaInvestimento(Investimento investimento) {
        Date inicioMes = obterInicioMesAtual();
        Date fimMes = obterFimMesAtual();

        boolean jaTemRendaNoMes = investimento.getRendas() != null &&
                investimento.getRendas().stream()
                        .anyMatch(renda -> renda.getData().compareTo(inicioMes) >= 0 &&
                                renda.getData().compareTo(fimMes) <= 0);

        if (jaTemRendaNoMes) {
            return;
        }

        double valorTotal = 0.0;
        if (investimento.getCompras() != null) {
            valorTotal = investimento.getCompras().stream()
                    .mapToDouble(CompraInvestimento::getValor)
                    .sum();
        }

        if (investimento.getRendas() != null) {
            valorTotal += investimento.getRendas().stream()
                    .mapToDouble(Renda::getValor)
                    .sum();
        }

        if (valorTotal > 0) {
            double valorRenda = valorTotal * investimento.getTaxaRendimentoMensal();

            Renda novaRenda = Renda.builder()
                    .titulo("Rendimento " + obterMesAnoAtual())
                    .valor(valorRenda)
                    .data(new Date())
                    .usuarioId(investimento.getUsuarioId())
                    .investimento(investimento)
                    .build();

            rendaRepository.save(novaRenda);
        }
    }

    @Transactional
    public void gerarRendaMensalManual(UUID investimentoId, String usuarioEmail) {
        Investimento investimento = repository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));

        gerarRendaMensalParaInvestimento(investimento);
    }

    private Date obterInicioMesAtual() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date obterFimMesAtual() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    private String obterMesAnoAtual() {
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH) + 1;
        int ano = cal.get(Calendar.YEAR);
        return String.format("%02d/%d", mes, ano);
    }

    private InvestimentoResponseDTO toResponseDTO(Investimento investimento) {
        double valorTotal = 0.0;
        double rendimentoAcumulado = 0.0;
        double valorAtualTotal = 0.0;
        double rentabilidade = 0.0;

        if (investimento.getCompras() != null && !investimento.getCompras().isEmpty()) {
            valorTotal = investimento.getCompras().stream()
                    .mapToDouble(CompraInvestimento::getValor)
                    .sum();
        }

        if (investimento.getRendas() != null && !investimento.getRendas().isEmpty()) {
            rendimentoAcumulado = investimento.getRendas().stream()
                    .mapToDouble(Renda::getValor)
                    .sum();
        }

        valorAtualTotal = valorTotal + rendimentoAcumulado;

        if (valorTotal > 0) {
            rentabilidade = (rendimentoAcumulado / valorTotal) * 100;
        }

        return InvestimentoResponseDTO.builder()
                .id(investimento.getId())
                .titulo(investimento.getTitulo())
                .tipo(investimento.getTipo())
                .taxaRendimentoMensal(investimento.getTaxaRendimentoMensal())
                .data(investimento.getData())
                .usuarioId(investimento.getUsuarioId())
                .valorTotal(valorTotal)
                .rendimentoAcumulado(rendimentoAcumulado)
                .valorAtualTotal(valorAtualTotal)
                .rentabilidade(rentabilidade)
                .build();
    }
}