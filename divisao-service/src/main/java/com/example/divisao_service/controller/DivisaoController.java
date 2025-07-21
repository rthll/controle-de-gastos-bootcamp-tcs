package com.example.divisao_service.controller;

import com.example.divisao_service.dto.DivisaoResponseDTO;
import com.example.divisao_service.dto.ListDivididosResponseDTO;
import com.example.divisao_service.dto.PendenteRequestDTO;
import com.example.divisao_service.dto.PendenteResponseDTO;
import com.example.divisao_service.entity.GastosDivididos;
import com.example.divisao_service.service.PendenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/divisao")
@RequiredArgsConstructor
public class DivisaoController {

    private final PendenteService pendenteService;

    @PostMapping
    public ResponseEntity<PendenteResponseDTO> criarDivisao(@RequestBody PendenteRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        PendenteResponseDTO response = pendenteService.criarPendencia(usuarioEmail, request.getUsuarioDoisId(), request.getIdGasto(), request.getValorDividido());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PendenteResponseDTO>> listarGastos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        List<PendenteResponseDTO> lista = pendenteService.listarPorUsuarioDois(usuarioEmail);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/aceitar")
    public ResponseEntity<DivisaoResponseDTO> aceitaDivisao(@RequestBody PendenteRequestDTO request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        DivisaoResponseDTO response = pendenteService.aceitaDivisao(request.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/negar")
    public void recusaDivisao(@RequestBody PendenteRequestDTO request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        pendenteService.recusaDivisao(request.getId());
    }

    @GetMapping("/listar/{idgasto}")
    public ResponseEntity<ListDivididosResponseDTO> listarDivididos(@PathVariable Long idgasto){
        ListDivididosResponseDTO divididos = pendenteService.listaUsuariosDivididos(idgasto);
        return ResponseEntity.ok(divididos);
    }

}
