package com.example.gastos_service.controller;

import com.example.gastos_service.dto.GastoRequestDTO;
import com.example.gastos_service.dto.GastoResponseDTO;
import com.example.gastos_service.service.GastoService;
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
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    @PostMapping
    public ResponseEntity<GastoResponseDTO> criarGasto(@RequestBody GastoRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        GastoResponseDTO response = gastoService.criarGasto(usuarioEmail, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{gastoId}/status")
    public ResponseEntity<GastoResponseDTO> alterarStatusAtivo(
            @PathVariable UUID gastoId,
            @RequestBody Map<String, Boolean> request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        Boolean novoStatus = request.get("ativo");
        if (novoStatus == null) {
            return ResponseEntity.badRequest().build();
        }

        GastoResponseDTO response = gastoService.alterarStatusAtivo(gastoId, novoStatus, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{gastoId}")
    public ResponseEntity<GastoResponseDTO> editarGasto(
            @PathVariable UUID gastoId,
            @RequestBody GastoRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        GastoResponseDTO response = gastoService.editarGasto(gastoId, dto, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{gastoId}/ativar")
    public ResponseEntity<GastoResponseDTO> ativarGasto(@PathVariable UUID gastoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        GastoResponseDTO response = gastoService.alterarStatusAtivo(gastoId, true, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{gastoId}/desativar")
    public ResponseEntity<GastoResponseDTO> desativarGasto(@PathVariable UUID gastoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        GastoResponseDTO response = gastoService.alterarStatusAtivo(gastoId, false, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GastoResponseDTO>> listarGastos(
            @RequestParam(required = false) Boolean ativo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        List<GastoResponseDTO> lista;

        if (ativo != null) {
            lista = gastoService.listarPorUsuarioComFiltroAtivo(usuarioEmail, ativo);
        } else {
            lista = gastoService.listarPorUsuario(usuarioEmail);
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<GastoResponseDTO>> listarGastosAtivos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        List<GastoResponseDTO> lista = gastoService.listarPorUsuarioComFiltroAtivo(usuarioEmail, true);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<GastoResponseDTO>> listarGastosInativos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        List<GastoResponseDTO> lista = gastoService.listarPorUsuarioComFiltroAtivo(usuarioEmail, false);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/total-mes-atual")
    public ResponseEntity<BigDecimal> getTotalMesAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        BigDecimal total = gastoService.calcularTotalGastosMesAtual(usuarioEmail);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total-futuro")
    public ResponseEntity<BigDecimal> getTotalFuturo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        BigDecimal total = gastoService.calcularTotalGastosFuturos(usuarioEmail);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total-por-mes")
    public ResponseEntity<Map<YearMonth, BigDecimal>> getTotalPorMes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        Map<YearMonth, BigDecimal> totalPorMes = gastoService.calcularTotalGastosPorMes(usuarioEmail);
        return ResponseEntity.ok(totalPorMes);
    }

    @GetMapping("/existe-categoria/{categoriaId}")
    public ResponseEntity<Boolean> existeGastoComCategoria(@PathVariable UUID categoriaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        boolean existe = gastoService.existeGastoComCategoria(categoriaId, usuarioEmail);
        return ResponseEntity.ok(existe);
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirGastos(@RequestBody List<UUID> gastoIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        try {
            gastoService.excluirGastos(gastoIds, usuarioEmail);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/desativar-lote")
    public ResponseEntity<Void> desativarGastos(@RequestBody List<UUID> gastoIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        try {
            gastoService.desativarGastos(gastoIds, usuarioEmail);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}