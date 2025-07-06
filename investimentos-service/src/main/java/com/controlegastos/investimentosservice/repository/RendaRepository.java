package com.controlegastos.investimentosservice.repository;

import com.controlegastos.investimentosservice.entity.Renda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RendaRepository extends JpaRepository<Renda, UUID> {

    List<Renda> findByUsuarioId(String usuarioId);

    List<Renda> findByInvestimentoId(UUID investimentoId);

    List<Renda> findByInvestimentoIdAndUsuarioId(UUID investimentoId, String usuarioId);

    Optional<Renda> findByIdAndUsuarioId(UUID id, String usuarioId);
}