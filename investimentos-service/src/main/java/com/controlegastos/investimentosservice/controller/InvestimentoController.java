package com.controlegastos.investimentosservice.controller;
import com.controlegastos.investimentosservice.entity.Investimento;
import com.controlegastos.investimentosservice.service.InvestimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/investimentos")
@RequiredArgsConstructor
public class InvestimentoController {

    private final InvestimentoService service;

    @GetMapping
    public List<Investimento> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Investimento> listarPorUsuario(@PathVariable String usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public Investimento criar(@RequestBody Investimento investimento) {
        return service.salvar(investimento);
    }

    @PutMapping("/{id}")
    public Investimento atualizar(@PathVariable UUID id, @RequestBody Investimento investimento) {
        return service.atualizar(id, investimento);
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }

    @GetMapping("/{id}/preco-medio")
    public double precoMedio(@PathVariable UUID id) {
        return service.calcularPrecoMedio(id);
    }

    @GetMapping("/{id}/valor-total")
    public double valorTotal(@PathVariable UUID id) {
        return service.calcularValorTotal(id);
    }

    @GetMapping("/{id}/rentabilidade")
    public double rentabilidade(@PathVariable UUID id, @RequestParam double valorAtual) {
        return service.calcularRentabilidade(id, valorAtual);
    }
}
