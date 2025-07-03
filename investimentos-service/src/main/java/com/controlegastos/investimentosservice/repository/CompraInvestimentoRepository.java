package com.controlegastos.investimentosservice.repository;
import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompraInvestimentoRepository extends JpaRepository<CompraInvestimento, UUID> {
}

