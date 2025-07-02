package com.controlegastos.gastosservice.repository;

import com.controlegastos.gastosservice.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ParcelaRepository extends JpaRepository<Parcela, UUID> {

    List<Parcela> findAllByGasto_UsuarioId(String usuarioId);

}

