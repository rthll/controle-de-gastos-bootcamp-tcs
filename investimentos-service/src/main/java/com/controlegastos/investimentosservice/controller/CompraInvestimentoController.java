package com.controlegastos.investimentosservice.controller;

import com.controlegastos.investimentosservice.dto.CompraInvestimentoRequestDTO;
import com.controlegastos.investimentosservice.dto.CompraInvestimentoResponseDTO;
import com.controlegastos.investimentosservice.service.CompraInvestimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraInvestimentoController {
    //refatorei todos os métodos para retornarem a DTO e não os dados diretamente e associar as açoes ao usuario autenticado (vale, no geral, pra investimento e renda também)
    private final CompraInvestimentoService compraInvestimentoService;

    @PostMapping
    public ResponseEntity<CompraInvestimentoResponseDTO> criarCompra(
            @RequestBody CompraInvestimentoRequestDTO dto,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        CompraInvestimentoResponseDTO response = compraInvestimentoService.criarCompra(usuarioEmail, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CompraInvestimentoResponseDTO>> listarCompras(Authentication authentication) {
        String usuarioEmail = authentication.getName();
        List<CompraInvestimentoResponseDTO> compras = compraInvestimentoService.listarComprasPorUsuario(usuarioEmail);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/investimento/{investimentoId}")
    public ResponseEntity<List<CompraInvestimentoResponseDTO>> listarComprasPorInvestimento(
            @PathVariable UUID investimentoId,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        List<CompraInvestimentoResponseDTO> compras = compraInvestimentoService.listarComprasPorInvestimento(investimentoId, usuarioEmail);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraInvestimentoResponseDTO> buscarCompra(
            @PathVariable UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        CompraInvestimentoResponseDTO compra = compraInvestimentoService.buscarPorId(id, usuarioEmail);
        return ResponseEntity.ok(compra);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCompra(
            @PathVariable UUID id,
            Authentication authentication) {
        String usuarioEmail = authentication.getName();
        compraInvestimentoService.deletar(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }
}