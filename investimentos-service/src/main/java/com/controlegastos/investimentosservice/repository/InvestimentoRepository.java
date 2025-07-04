package com.controlegastos.investimentosservice.repository;
import com.controlegastos.investimentosservice.entity.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvestimentoRepository extends JpaRepository<Investimento, UUID> {
    List<Investimento> findByUsuarioId(String usuarioId);
}
