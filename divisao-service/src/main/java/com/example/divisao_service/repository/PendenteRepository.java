package com.example.divisao_service.repository;

import com.example.divisao_service.entity.Pendente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PendenteRepository extends JpaRepository<Pendente, UUID> {
    List<Pendente> findByUsuarioDoisId(String usuarioDoisId);
    boolean findByIdGasto(UUID idGasto);

    boolean existsByIdGasto(UUID idGasto);
}
