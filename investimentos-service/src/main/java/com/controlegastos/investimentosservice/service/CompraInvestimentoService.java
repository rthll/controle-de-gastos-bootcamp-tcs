package com.controlegastos.investimentosservice.service;
import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import com.controlegastos.investimentosservice.repository.CompraInvestimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompraInvestimentoService {

    private final CompraInvestimentoRepository repository;

    public List<CompraInvestimento> listarTodos() {
        return repository.findAll();
    }

    public CompraInvestimento salvar(CompraInvestimento compra) {
        return repository.save(compra);
    }

    public CompraInvestimento atualizar(UUID id, CompraInvestimento compra) {
        compra.setId(id);
        return repository.save(compra);
    }

    public void deletar(UUID id) {
        repository.deleteById(id);
    }

}
