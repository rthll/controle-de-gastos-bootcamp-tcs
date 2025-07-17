package com.example.categorias_services.service;

import com.example.categorias_services.client.GastoClient;
import com.example.categorias_services.dto.CategoriaRequestDTO;
import com.example.categorias_services.dto.CategoriaResponseDTO;
import com.example.categorias_services.entity.Categoria;
import com.example.categorias_services.exception.CategoriaMembershipException;
import com.example.categorias_services.exception.CategoriaNotBelongToUser;
import com.example.categorias_services.exception.CategoriaNotFoundException;
import com.example.categorias_services.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final GastoClient gastoClient;
    private final TokenService tokenService;

    public CategoriaResponseDTO criarCategoria(String usuarioEmail, CategoriaRequestDTO dto) {
        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .usuarioId(usuarioEmail)
                .limiteDeGasto(dto.getLimiteDeGasto())
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

    public CategoriaResponseDTO buscarPorId(Long id, String usuarioEmail) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria não encontrada"));

        if (!categoria.getUsuarioId().equals(usuarioEmail)) {
            throw new CategoriaNotBelongToUser("Categoria não pertence ao usuário");
        }

        return toResponseDTO(categoria);
    }

    public CategoriaResponseDTO atualizarCategoria(Long id, String usuarioEmail, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria não encontrada"));

        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setLimiteDeGasto(dto.getLimiteDeGasto());

        categoria = categoriaRepository.save(categoria);

        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .limiteDeGasto(categoria.getLimiteDeGasto())
                .usuarioId(usuarioEmail)
                .build();
    }

    public void deletar(Long id, String usuarioEmail) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria não encontrada"));

        if (!categoria.getUsuarioId().equals(usuarioEmail)) {
            throw new CategoriaNotBelongToUser("Categoria não pertence ao usuário");
        }

        String token = tokenService.getCurrentToken();
        if (gastoClient.existeGastoComCategoria(id, token)) {
            throw new CategoriaMembershipException("Não é possível excluir a categoria pois existem gastos associados a ela");
        }

        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .limiteDeGasto(categoria.getLimiteDeGasto())
                .usuarioId(categoria.getUsuarioId())
                .build();
    }

    public CategoriaResponseDTO buscarPorNome(String nome, String usuarioEmail) {
        Categoria categoria = categoriaRepository.findByNomeAndUsuarioId(nome, usuarioEmail);
        return toResponseDTO(categoria);
    }
}