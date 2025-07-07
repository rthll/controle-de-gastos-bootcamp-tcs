package com.controlegastos.investimentosservice.service;

import com.controlegastos.investimentosservice.dto.InvestimentoRequestDTO;
import com.controlegastos.investimentosservice.dto.InvestimentoResponseDTO;
import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import com.controlegastos.investimentosservice.entity.Investimento;
import com.controlegastos.investimentosservice.repository.InvestimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestimentoService {
    private final InvestimentoRepository repository;

    public InvestimentoResponseDTO criarInvestimento(String usuarioEmail, InvestimentoRequestDTO dto) {
        Investimento investimento = Investimento.builder()
                .titulo(dto.getTitulo())
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
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
        return toResponseDTO(investimento);
    }

    public void deletar(UUID id, String usuarioEmail) {
        Investimento investimento = repository.findByIdAndUsuarioId(id, usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
        repository.deleteById(id);
    }

    public double calcularPrecoMedio(UUID investimentoId, String usuarioEmail) {
        Investimento inv = repository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new NoSuchElementException("Investimento não encontrado"));

        List<CompraInvestimento> compras = inv.getCompras();
        if (compras == null || compras.isEmpty()) {
            return 0;
        }

        double totalValor = 0;
        int totalQuantidade = 0;

        for (CompraInvestimento compra : compras) {
            totalValor += compra.getValor() * compra.getQuantidade();
            totalQuantidade += compra.getQuantidade();
        }

        return totalQuantidade == 0 ? 0 : totalValor / totalQuantidade;
    }

    public double calcularValorTotal(UUID investimentoId, String usuarioEmail) {
        Investimento inv = repository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new NoSuchElementException("Investimento não encontrado"));

        if (inv.getCompras() == null || inv.getCompras().isEmpty()) {
            return 0;
        }

        return inv.getCompras().stream()
                .mapToDouble(c -> c.getValor() * c.getQuantidade())
                .sum();
    }

    public double calcularRentabilidade(UUID investimentoId, double valorAtualPorTitulo, String usuarioEmail) {
        Investimento inv = repository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new NoSuchElementException("Investimento não encontrado"));

        if (inv.getCompras() == null || inv.getCompras().isEmpty()) {
            return 0;
        }

        int totalQuantidade = inv.getCompras().stream().mapToInt(CompraInvestimento::getQuantidade).sum();
        double valorInvestido = calcularValorTotal(investimentoId, usuarioEmail);
        double valorAtual = valorAtualPorTitulo * totalQuantidade;

        if (valorInvestido == 0) return 0;
        return ((valorAtual - valorInvestido) / valorInvestido) * 100;
    }

    private InvestimentoResponseDTO toResponseDTO(Investimento investimento) {
        double precoMedio = 0;
        double valorTotal = 0;
        int quantidadeTotal = 0;

        if (investimento.getCompras() != null && !investimento.getCompras().isEmpty()) {
            for (CompraInvestimento compra : investimento.getCompras()) {
                valorTotal += compra.getValor() * compra.getQuantidade();
                quantidadeTotal += compra.getQuantidade();
            }
            precoMedio = quantidadeTotal == 0 ? 0 : valorTotal / quantidadeTotal;
        }

        return InvestimentoResponseDTO.builder()
                .id(investimento.getId())
                .titulo(investimento.getTitulo())
                .data(investimento.getData())
                .usuarioId(investimento.getUsuarioId())
                .precoMedio(precoMedio)
                .valorTotal(valorTotal)
                .quantidadeTotal(quantidadeTotal)
                .build();
    }
}