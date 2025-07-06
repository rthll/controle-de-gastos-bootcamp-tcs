package com.controlegastos.investimentosservice.repository;

import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompraInvestimentoRepository extends JpaRepository<CompraInvestimento, UUID> {

    List<CompraInvestimento> findByUsuarioId(String usuarioId);

    List<CompraInvestimento> findByInvestimentoId(UUID investimentoId);

    List<CompraInvestimento> findByInvestimentoIdAndUsuarioId(UUID investimentoId, String usuarioId);

    Optional<CompraInvestimento> findByIdAndUsuarioId(UUID id, String usuarioId);
}