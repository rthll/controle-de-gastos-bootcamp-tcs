package com.controlegastos.investimentosservice.controller;

import com.controlegastos.investimentosservice.dto.RendaResponseDTO;
import com.controlegastos.investimentosservice.service.RendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rendas")
@RequiredArgsConstructor
public class RendaController {

    private final RendaService rendaService;

    @GetMapping
    public ResponseEntity<List<RendaResponseDTO>> listarRendas(Authentication authentication) {
        String usuarioEmail = authentication.getName();
        List<RendaResponseDTO> rendas = rendaService.listarRendasPorUsuario(usuarioEmail);
        return ResponseEntity.ok(rendas);
    }

    @GetMapping("/investimento/{investimentoId}")
    public ResponseEntity<List<RendaResponseDTO>> listarRendasPorInvestimento(
            @PathVariable("investimentoId") UUID investimentoId,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        List<RendaResponseDTO> rendas = rendaService.listarRendasPorInvestimento(investimentoId, usuarioEmail);
        return ResponseEntity.ok(rendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendaResponseDTO> buscarRenda(
            @PathVariable("id") UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        RendaResponseDTO renda = rendaService.buscarPorId(id, usuarioEmail);
        return ResponseEntity.ok(renda);
    }

}