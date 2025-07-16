package com.example.divisao_service.repository;

import com.example.divisao_service.entity.GastosDivididos;
import com.example.divisao_service.entity.Pendente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GastosDivididosRepository extends JpaRepository<GastosDivididos, Long> {
    List<GastosDivididos> findByIdGasto(Long idGasto);
}
