package com.controlegastos.investimentosservice.controller;

import com.controlegastos.investimentosservice.dto.InvestimentoRequestDTO;
import com.controlegastos.investimentosservice.dto.InvestimentoResponseDTO;
import com.controlegastos.investimentosservice.service.InvestimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/investimentos")
@RequiredArgsConstructor
@Validated
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    @PostMapping
    public ResponseEntity<InvestimentoResponseDTO> criarInvestimento(
            @Valid @RequestBody InvestimentoRequestDTO dto,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        InvestimentoResponseDTO response = investimentoService.criarInvestimento(usuarioEmail, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InvestimentoResponseDTO>> listarInvestimentos(Authentication authentication) {
        String usuarioEmail = authentication.getName();
        List<InvestimentoResponseDTO> investimentos = investimentoService.listarInvestimentosPorUsuario(usuarioEmail);
        return ResponseEntity.ok(investimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestimentoResponseDTO> buscarInvestimento(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        InvestimentoResponseDTO investimento = investimentoService.buscarPorId(id, usuarioEmail);
        return ResponseEntity.ok(investimento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInvestimento(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        investimentoService.deletar(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/valor-total")
    public ResponseEntity<Double> calcularValorTotal(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        double valorTotal = investimentoService.calcularValorTotal(id, usuarioEmail);
        return ResponseEntity.ok(valorTotal);
    }

    @GetMapping("/{id}/valor-atual-total")
    public ResponseEntity<Double> calcularValorAtualTotal(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        double valorAtualTotal = investimentoService.calcularValorAtualTotal(id, usuarioEmail);
        return ResponseEntity.ok(valorAtualTotal);
    }

    @GetMapping("/{id}/rendimento-acumulado")
    public ResponseEntity<Double> calcularRendimentoAcumulado(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        double rendimentoAcumulado = investimentoService.calcularRendimentoAcumulado(id, usuarioEmail);
        return ResponseEntity.ok(rendimentoAcumulado);
    }

    @GetMapping("/{id}/rendimento-mensal")
    public ResponseEntity<Double> calcularRendimentoMensal(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        double rendimentoMensal = investimentoService.calcularRendimentoMensal(id, usuarioEmail);
        return ResponseEntity.ok(rendimentoMensal);
    }

    //pra gerar renda manualmente para teste ===***REMOVER APOS TESTES***===
    @PostMapping("/{id}/gerar-renda")
    public ResponseEntity<Void> gerarRendaMensal(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        investimentoService.gerarRendaMensalManual(id, usuarioEmail);
        return ResponseEntity.ok().build();
    }
}