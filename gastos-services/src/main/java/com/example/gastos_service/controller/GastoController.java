package com.example.gastos_service.controller;

import com.example.gastos_service.dto.GastoRequestDTO;
import com.example.gastos_service.dto.GastoResponseDTO;
import com.example.gastos_service.dto.TotalPorMesDTO;
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
import java.util.stream.Collectors;

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
            @PathVariable Long gastoId,
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

    @PostMapping("/buscar-por-ids")
    public ResponseEntity<List<GastoResponseDTO>> buscarGastosPorIds(
            @RequestBody List<Long> gastoIds) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        System.out.println("IDs: " + gastoIds);
        System.out.println("Usuário: " + usuarioEmail);

        List<GastoResponseDTO> gastos = gastoService.buscarGastosPorIds(gastoIds, usuarioEmail);

        return ResponseEntity.ok(gastos);
    }

    @PatchMapping("/{gastoId}")
    public ResponseEntity<GastoResponseDTO> editarGasto(
            @PathVariable Long gastoId,
            @RequestBody GastoRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        GastoResponseDTO response = gastoService.editarGasto(gastoId, dto, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{gastoId}/ativar")
    public ResponseEntity<GastoResponseDTO> ativarGasto(@PathVariable Long gastoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        GastoResponseDTO response = gastoService.alterarStatusAtivo(gastoId, true, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{gastoId}/desativar")
    public ResponseEntity<GastoResponseDTO> desativarGasto(@PathVariable Long gastoId) {
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

    @GetMapping("/total-por-mes-por-gasto")
    public ResponseEntity<List<TotalPorMesDTO>> getTotalPorMesPorGasto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        List<TotalPorMesDTO> totais = gastoService.calcularTotalGastosPorMesDTO(usuarioEmail);
        return ResponseEntity.ok(totais);
    }


    @GetMapping("/existe-categoria/{categoriaId}")
    public ResponseEntity<Boolean> existeGastoComCategoria(@PathVariable Long categoriaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        boolean existe = gastoService.existeGastoComCategoria(categoriaId, usuarioEmail);
        return ResponseEntity.ok(existe);
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirGastos(@RequestBody List<Long> gastoIds) {
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
    public ResponseEntity<Void> desativarGastos(@RequestBody List<Long> gastoIds) {
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

    @GetMapping("/divisao/{id}")
    public ResponseEntity<GastoResponseDTO> buscarPorId(@PathVariable Long id) {
        GastoResponseDTO categoria = gastoService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping("/divisao")
    public ResponseEntity<GastoResponseDTO> criarGastoTeste(@RequestBody GastoRequestDTO dto) {
        GastoResponseDTO response = gastoService.criarGastoDivisao(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/divisao/{idgasto}")
    public void deletarGastoById(@PathVariable Long idgasto){
        gastoService.deletById(idgasto);
    }

    @PutMapping("/divisao")
    public ResponseEntity<Void> atualizarValor(@RequestBody Map<String, Object> payload) {
        Long id = Long.parseLong(payload.get("id").toString());
        BigDecimal valor = new BigDecimal(payload.get("valor").toString());

        gastoService.atualizarValor(id, valor);
        return ResponseEntity.ok().build();
    }

}