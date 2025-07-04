package com.controlegastos.investimentosservice.service;

import com.controlegastos.investimentosservice.entity.Renda;
import com.controlegastos.investimentosservice.repository.RendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RendaService {

    private final RendaRepository repository;

    public List<Renda> listarTodos() {
        return repository.findAll();
    }

    public Renda salvar(Renda renda) {
        return repository.save(renda);
    }

    public Renda atualizar(UUID id, Renda renda) {
        renda.setId(id);
        return repository.save(renda);
    }

    public void deletar(UUID id) {
        repository.deleteById(id);
    }
}

