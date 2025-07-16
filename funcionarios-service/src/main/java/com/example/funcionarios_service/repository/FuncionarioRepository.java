package com.example.funcionarios_service.repository;

import com.example.funcionarios_service.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByUsuarioId(String usuarioId);
}
