package com.example.funcionarios_service.controller;

import com.example.funcionarios_service.dto.FuncionarioRequestDTO;
import com.example.funcionarios_service.dto.FuncionarioResponseDTO;
import com.example.funcionarios_service.dto.SalariosPorSetorDTO;
import com.example.funcionarios_service.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> criarFuncionario(@RequestBody FuncionarioRequestDTO dto) {
        String usuarioEmail = getUsuarioEmail();
        FuncionarioResponseDTO response = funcionarioService.criarFuncionario(usuarioEmail, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{funcionariId}/ativar")
    public ResponseEntity<FuncionarioResponseDTO> ativarFuncionario(@PathVariable Long funcionariId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        FuncionarioResponseDTO response = funcionarioService.alterarStatusAtivo(funcionariId, true, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{funcionariId}/desativar")
    public ResponseEntity<FuncionarioResponseDTO> desativarFuncionario(@PathVariable Long funcionariId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        FuncionarioResponseDTO response = funcionarioService.alterarStatusAtivo(funcionariId, false, usuarioEmail);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{funcionarioId}")
    public ResponseEntity<FuncionarioResponseDTO> editarFuncionario(
            @PathVariable Long funcionarioId,
            @RequestBody FuncionarioRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        FuncionarioResponseDTO response = funcionarioService.editarFuncionario(funcionarioId, dto, usuarioEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarFuncionariosAtivos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        List<FuncionarioResponseDTO> lista = funcionarioService.listarPorUsuarioComFiltroAtivo(usuarioEmail, true);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarFuncionariosInativos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        List<FuncionarioResponseDTO> lista = funcionarioService.listarPorUsuarioComFiltroAtivo(usuarioEmail, false);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/total-salarios-mes")
    public ResponseEntity<BigDecimal> getTotalMes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        BigDecimal total = funcionarioService.calcularTotalSalariosMensal(usuarioEmail);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/existe-setor/{setorId}")
    public ResponseEntity<Boolean> existeFuncionarioComSetor(@PathVariable Long setorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        boolean existe = funcionarioService.existeFuncionarioComSetor(setorId, usuarioEmail);
        return ResponseEntity.ok(existe);
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirFuncionarios(@RequestBody List<Long> funcionarioIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        try {
            funcionarioService.excluirFuncionarios(funcionarioIds, usuarioEmail);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/desativar-lote")
    public ResponseEntity<Void> desativarFuncionarios(@RequestBody List<Long> funcionariosIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        try {
            funcionarioService.desativarFuncionarios(funcionariosIds, usuarioEmail);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getUsuarioEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/total-salarios-por-setor")
    public ResponseEntity<List<SalariosPorSetorDTO>> getTotalSalariosPorSetor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        List<SalariosPorSetorDTO> totaisPorSetor = funcionarioService.calcularTotalSalariosPorSetor(usuarioEmail);
        return ResponseEntity.ok(totaisPorSetor);
    }

    @PostMapping("/buscar-por-ids")
    public ResponseEntity<List<FuncionarioResponseDTO>> buscarFuncionariosPorIds(
            @RequestBody List<Long> funcionarioIds) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();

        System.out.println("IDs: " + funcionarioIds);
        System.out.println("Usuário: " + usuarioEmail);

        List<FuncionarioResponseDTO> gastos = funcionarioService.buscarFuncionariosPorIds(funcionarioIds, usuarioEmail);

        return ResponseEntity.ok(gastos);
    }
}
