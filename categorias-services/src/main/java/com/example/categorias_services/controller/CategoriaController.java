package com.example.categorias_services.controller;

import com.example.categorias_services.dto.CategoriaRequestDTO;
import com.example.categorias_services.dto.CategoriaResponseDTO;
import com.example.categorias_services.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody CategoriaRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        CategoriaResponseDTO response = categoriaService.criarCategoria(usuarioEmail, dto);
        return ResponseEntity.ok(response);
    }

    // mudei aqui pra usar o email do context
    @GetMapping("/usuario")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategoriasPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName(); // Email vem do JWT
        List<CategoriaResponseDTO> categorias = categoriaService.listarCategoriasPorUsuario(usuarioEmail);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        CategoriaResponseDTO categoria = categoriaService.buscarPorId(id, usuarioEmail);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        categoriaService.deletar(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorNome(@PathVariable String nome) {
        CategoriaResponseDTO categoria = categoriaService.buscarPorNome(nome);
        return ResponseEntity.ok(categoria);
    }
}