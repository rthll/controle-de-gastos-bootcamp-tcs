package com.example.divisao_service.service;

import com.example.divisao_service.client.GastoClient;
import com.example.divisao_service.dto.*;
import com.example.divisao_service.entity.Pendente;
import com.example.divisao_service.repository.PendenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendenteService {

    private final PendenteRepository pendenteRepository;
    private final GastoClient gastoClient;
    private final TokenService tokenService; // Serviço para gerenciar tokens

    public PendenteResponseDTO criarPendencia(String usuarioUmEmail, String usuarioDoisEmail, UUID idGasto ){
//        Buscar o gasto por ID
        String token = tokenService.getCurrentToken();
        GastoDTO gasto = gastoClient.gastoExiste(idGasto, token);
        if (gasto == null) {
            throw new RuntimeException("Gasto não encontrado");
        }

        BigDecimal valorDividido = gasto.getValorTotal().divide(BigDecimal.valueOf(2));
        Pendente pendente = Pendente.builder()
                .descricao(gasto.getDescricao())
                .valorTotal(valorDividido)
                .data(gasto.getData())
                .usuarioUmId(usuarioUmEmail) // <- associa o usuário que criou o gasto
                .usuarioDoisId(usuarioDoisEmail) // <- associa ao usuario que dividira o gasto
                .idGasto(idGasto)
                .fonte(gasto.getFonte())
                .categoriaId(gasto.getCategoriaId())
                .build();

        Pendente salvo = pendenteRepository.save(pendente);
        return mapToResponseDTO(salvo);
    }

    private PendenteResponseDTO mapToResponseDTO(Pendente pendente) {
        return PendenteResponseDTO.builder()
                .id(pendente.getId())
                .descricao(pendente.getDescricao())
                .valorTotal(pendente.getValorTotal())
                .data(pendente.getData())
                .usuarioUmId(pendente.getUsuarioUmId())
                .usuarioDoisId(pendente.getUsuarioDoisId())
                .idGasto(pendente.getIdGasto())
                .fonte(pendente.getFonte())
                .categoriaId(pendente.getCategoriaId())
                .build();
    }

    public List<PendenteResponseDTO> listarPorUsuarioDois(String usuarioEmail) {
        return pendenteRepository.findByUsuarioDoisId(usuarioEmail)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public DivisaoResponseDTO aceitaDivisao(UUID id) {
        Pendente pendencia = pendenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pendencia não encontrada"));

        String token = tokenService.getCurrentToken();

        GastoDTO gastoUm = GastoDTO.builder()
                .descricao(pendencia.getDescricao())
                .valorTotal(pendencia.getValorTotal())
                .data(pendencia.getData())
                .parcelado(false)
                .numeroParcelas(1)
                .usuarioId(pendencia.getUsuarioUmId())
                .fonte(pendencia.getFonte())
                .categoriaId(pendencia.getCategoriaId())
                .build();
        GastoDTO gastoDividido = gastoClient.divideGastos(gastoUm, token);

//        Criar categoria de divisao para o usuario2
        CategoriaDTO categoria = CategoriaDTO.builder()
                .nome("Divisao")
                .descricao("Divisao de gastos")
                .usuarioId(pendencia.getUsuarioDoisId())
                .build();
        CategoriaDTO novaCategoria = gastoClient.criarCategoria(categoria, token);

        GastoDTO gastoDois = GastoDTO.builder()
                .descricao(pendencia.getDescricao())
                .valorTotal(pendencia.getValorTotal())
                .data(pendencia.getData())
                .parcelado(false)
                .numeroParcelas(1)
                .usuarioId(pendencia.getUsuarioDoisId())
                .fonte(pendencia.getFonte())
                .categoriaId(novaCategoria.getId())
                .build();
        GastoDTO gastoDivididoDois = gastoClient.divideGastos(gastoDois, token);

        pendenteRepository.deleteById(id);
        gastoClient.deletarGastoById(pendencia.getIdGasto());

        return DivisaoResponseDTO.builder()
                .gastoUsuarioUm(gastoDividido)
                .gastoUsuarioDois(gastoDivididoDois)
                .build();
    }
}
