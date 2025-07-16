package com.example.divisao_service.repository;

import com.example.divisao_service.entity.Pendente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PendenteRepository extends JpaRepository<Pendente, Long> {
    List<Pendente> findByUsuarioDoisId(String usuarioDoisId);
    boolean existsByIdGasto(Long idGasto);
}
