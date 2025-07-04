package com.controlegastos.investimentosservice.controller;
import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import com.controlegastos.investimentosservice.service.CompraInvestimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraInvestimentoController {

    private final CompraInvestimentoService service;

    @GetMapping
    public List<CompraInvestimento> listarTodos(){
        return service.listarTodos();
    }

    @PostMapping
    public CompraInvestimento criar(@RequestBody CompraInvestimento compra){
        return service.salvar(compra);
    }

    @PutMapping("/{id}")
    public CompraInvestimento atualizar(@PathVariable UUID id, @RequestBody CompraInvestimento compra){
        return service.atualizar(id, compra);
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
