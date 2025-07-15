package com.example.gastos_service.repository;

import com.example.gastos_service.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findAllByGasto_UsuarioId(String usuarioId);

}

