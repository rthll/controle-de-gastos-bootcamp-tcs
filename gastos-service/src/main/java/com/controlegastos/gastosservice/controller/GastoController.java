package com.controlegastos.gastosservice.controller;

import com.controlegastos.gastosservice.dto.GastoRequestDTO;
import com.controlegastos.gastosservice.dto.GastoResponseDTO;
import com.controlegastos.gastosservice.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    // 🟢 POST /gastos
    @PostMapping
    public ResponseEntity<GastoResponseDTO> criarGasto(
            @RequestHeader("usuario-id") String usuarioId,
            @RequestBody GastoRequestDTO dto) {

        GastoResponseDTO response = gastoService.criarGasto(usuarioId, dto);
        return ResponseEntity.ok(response);
    }

    // 🔵 GET /gastos
    @GetMapping
    public ResponseEntity<List<GastoResponseDTO>> listarGastos(
            @RequestHeader("usuario-id") String usuarioId) {

        List<GastoResponseDTO> lista = gastoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(lista);
    }
}
