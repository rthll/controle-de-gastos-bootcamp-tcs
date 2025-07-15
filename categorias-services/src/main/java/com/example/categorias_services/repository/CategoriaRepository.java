package com.example.categorias_services.repository;

import com.example.categorias_services.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByUsuarioId(String usuarioId);
    Categoria findByNomeAndUsuarioId(String nome, String usuarioId);
}
