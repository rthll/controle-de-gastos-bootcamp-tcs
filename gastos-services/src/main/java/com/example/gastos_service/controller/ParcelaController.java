package com.example.gastos_service.controller;

import com.example.gastos_service.dto.ParcelaDTO;
import com.example.gastos_service.service.ParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parcelas")
@RequiredArgsConstructor
public class ParcelaController {

    private final ParcelaService parcelaService;

    @GetMapping
    public ResponseEntity<List<ParcelaDTO>> listarParcelas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName(); // vem do JWT

        List<ParcelaDTO> parcelas = parcelaService.listarParcelasDoUsuario(usuarioEmail);
        return ResponseEntity.ok(parcelas);
    }
}
