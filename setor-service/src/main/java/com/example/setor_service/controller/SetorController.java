package com.example.setor_service.controller;


import com.example.setor_service.dto.SetorRequestDTO;
import com.example.setor_service.dto.SetorResponseDTO;
import com.example.setor_service.service.SetorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/setores")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService setorService;

    @PostMapping
    public ResponseEntity<SetorResponseDTO> criarSetor(@RequestBody SetorRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        SetorResponseDTO response = setorService.criarSetor(usuarioEmail, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<SetorResponseDTO>> listarSetoresPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        List<SetorResponseDTO> setores = setorService.listarSetoresPorUsuario(usuarioEmail);
        return ResponseEntity.ok(setores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetorResponseDTO> buscarPorId(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        SetorResponseDTO setor = setorService.buscarPorId(id, usuarioEmail);
        return ResponseEntity.ok(setor);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<SetorResponseDTO> buscarPorNome(@PathVariable String nome) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        SetorResponseDTO setor = setorService.buscarPorNome(nome, usuarioEmail);
        return ResponseEntity.ok(setor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetorResponseDTO> atualizar(@PathVariable Long id, @RequestBody SetorRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        SetorResponseDTO atualizado = setorService.atualizarSetor(id, usuarioEmail, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioEmail = authentication.getName();
        setorService.deletar(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }
}

