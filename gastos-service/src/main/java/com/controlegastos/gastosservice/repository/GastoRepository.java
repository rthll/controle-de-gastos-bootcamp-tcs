package com.controlegastos.gastosservice.repository;

import com.controlegastos.gastosservice.entity.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, UUID> {
    List<Gasto> findByUsuarioId(String usuarioId);
}
