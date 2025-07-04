package com.controlegastos.investimentosservice.controller;

import com.controlegastos.investimentosservice.entity.Renda;
import com.controlegastos.investimentosservice.service.RendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rendas")
@RequiredArgsConstructor
public class RendaController {

    private final RendaService service;

    //Listar
    @GetMapping
    public List<Renda> listarTodos() {
        return service.listarTodos();
    }

    //Criar
    @PostMapping
    public Renda criar(@RequestBody Renda renda) {
        return service.salvar(renda);
    }

    //Atualizar
    @PutMapping("/{id}")
    public Renda atualizar(@PathVariable UUID id, @RequestBody Renda renda) {
        return service.atualizar(id, renda);
    }

    //Deletar
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
