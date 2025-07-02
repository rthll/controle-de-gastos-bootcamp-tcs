package com.controlegastos.gastosservice.controller;

import com.controlegastos.gastosservice.dto.ParcelaDTO;
import com.controlegastos.gastosservice.service.ParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
