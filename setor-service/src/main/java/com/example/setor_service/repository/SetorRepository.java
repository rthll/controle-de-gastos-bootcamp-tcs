package com.example.setor_service.repository;

import com.example.setor_service.entity.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SetorRepository extends JpaRepository<Setor, Long> {

    List<Setor> findByUsuarioId(String usuarioId);
    Optional<Setor> findByNomeAndUsuarioId(String nome, String usuarioId);
    boolean existsByNomeAndUsuarioId(String nome, String usuarioId);
}

