package com.controlegastos.investimentosservice.service;
import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import com.controlegastos.investimentosservice.entity.Investimento;
import com.controlegastos.investimentosservice.repository.InvestimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvestimentoService {
    private final InvestimentoRepository repository;

    public List<Investimento> listarTodos() {
        return repository.findAll();
    }

    public Investimento salvar(Investimento investimento) {
        return repository.save(investimento);
    }

    public void deletar(UUID id) {
        repository.deleteById(id);
    }

    public double calcularPrecoMedio(UUID investimentoId) {
        Investimento inv = repository.findById(investimentoId).orElseThrow(() -> new NoSuchElementException("Investimento não encontrado"));
        List<CompraInvestimento> compras = inv.getCompras();

        double totalValor = 0;
        int totalQuantidade = 0;

        for (CompraInvestimento compra : compras) {
            totalValor += compra.getValor() * compra.getQuantidade();
            totalQuantidade += compra.getQuantidade();
        }

        return totalQuantidade == 0 ? 0 : totalValor / totalQuantidade;
    }

    public double calcularValorTotal(UUID investimentoId) {
        Investimento inv = repository.findById(investimentoId).orElseThrow(() -> new NoSuchElementException("Investimento não encontrado"));
        return inv.getCompras().stream()
                .mapToDouble(c -> c.getValor() * c.getQuantidade())
                .sum();
    }

    public double calcularRentabilidade(UUID investimentoId, double valorAtualPorTitulo) {
        Investimento inv = repository.findById(investimentoId).orElseThrow(() -> new NoSuchElementException("Investimento não encontrado"));
        int totalQuantidade = inv.getCompras().stream().mapToInt(CompraInvestimento::getQuantidade).sum();
        double valorInvestido = calcularValorTotal(investimentoId);
        double valorAtual = valorAtualPorTitulo * totalQuantidade;

        if (valorInvestido == 0) return 0;
        return ((valorAtual - valorInvestido) / valorInvestido) * 100;
    }
}




}
