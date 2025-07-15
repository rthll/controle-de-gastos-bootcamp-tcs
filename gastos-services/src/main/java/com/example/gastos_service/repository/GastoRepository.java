package com.example.gastos_service.repository;

import com.example.gastos_service.entity.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByUsuarioId(String usuarioId);
}
