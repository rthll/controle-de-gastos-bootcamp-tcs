package com.controlegastos.investimentosservice.controller;

import com.controlegastos.investimentosservice.dto.InvestimentoRequestDTO;
import com.controlegastos.investimentosservice.dto.InvestimentoResponseDTO;
import com.controlegastos.investimentosservice.dto.RentabilidadeRequestDTO;
import com.controlegastos.investimentosservice.service.InvestimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/investimentos")
@RequiredArgsConstructor
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    @PostMapping
    public ResponseEntity<InvestimentoResponseDTO> criarInvestimento(
            @RequestBody InvestimentoRequestDTO dto,
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
            @PathVariable UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        InvestimentoResponseDTO investimento = investimentoService.buscarPorId(id, usuarioEmail);
        return ResponseEntity.ok(investimento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInvestimento(
            @PathVariable UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        investimentoService.deletar(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/preco-medio")
    public ResponseEntity<Double> calcularPrecoMedio(
            @PathVariable UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        double precoMedio = investimentoService.calcularPrecoMedio(id, usuarioEmail);
        return ResponseEntity.ok(precoMedio);
    }

    @GetMapping("/{id}/valor-total")
    public ResponseEntity<Double> calcularValorTotal(
            @PathVariable UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        double valorTotal = investimentoService.calcularValorTotal(id, usuarioEmail);
        return ResponseEntity.ok(valorTotal);
    }

    @PostMapping("/rentabilidade")
    public ResponseEntity<Double> calcularRentabilidade(
            @RequestBody RentabilidadeRequestDTO dto,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        double rentabilidade = investimentoService.calcularRentabilidade(
                dto.getInvestimentoId(),
                dto.getValorAtualPorTitulo(),
                usuarioEmail
        );
        return ResponseEntity.ok(rentabilidade);
    }
}