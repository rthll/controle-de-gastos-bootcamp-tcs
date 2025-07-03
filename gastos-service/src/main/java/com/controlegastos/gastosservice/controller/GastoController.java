package com.controlegastos.gastosservice.controller;

import com.controlegastos.gastosservice.dto.GastoRequestDTO;
import com.controlegastos.gastosservice.dto.GastoResponseDTO;
import com.controlegastos.gastosservice.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    @PostMapping
    public ResponseEntity<GastoResponseDTO> criarGasto(@RequestBody GastoRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        String usuarioEmail = authentication.getName();

        GastoResponseDTO response = gastoService.criarGasto(usuarioEmail, dto);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<GastoResponseDTO>> listarGastos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        List<GastoResponseDTO> lista = gastoService.listarPorUsuario(usuarioEmail);
        return ResponseEntity.ok(lista);
    }
}
