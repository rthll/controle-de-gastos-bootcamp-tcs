package com.example.categorias_services.service;

import com.example.categorias_services.dto.CategoriaRequestDTO;
import com.example.categorias_services.dto.CategoriaResponseDTO;
import com.example.categorias_services.entity.Categoria;
import com.example.categorias_services.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaResponseDTO criarCategoria(String usuarioEmail, CategoriaRequestDTO dto) {
        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .usuarioId(usuarioEmail)
                .build();

        categoria = categoriaRepository.save(categoria);
        return toResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> listarCategoriasPorUsuario(String usuarioEmail) {
        return categoriaRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO buscarPorId(UUID id, String usuarioEmail) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!categoria.getUsuarioId().equals(usuarioEmail)) {
            throw new RuntimeException("Categoria não pertence ao usuário");
        }

        return toResponseDTO(categoria);
    }

    public void deletar(UUID id, String usuarioEmail) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!categoria.getUsuarioId().equals(usuarioEmail)) {
            throw new RuntimeException("Categoria não pertence ao usuário");
        }

        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .usuarioId(categoria.getUsuarioId())
                .build();
    }
}