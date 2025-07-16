package com.example.divisao_service.service;

import com.example.divisao_service.client.GastoClient;
import com.example.divisao_service.dto.*;
import com.example.divisao_service.entity.Pendente;
import com.example.divisao_service.repository.PendenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendenteService {

    private final PendenteRepository pendenteRepository;
    private final GastoClient gastoClient;
    private final TokenService tokenService; // Serviço para gerenciar tokens

    public PendenteResponseDTO criarPendencia(String usuarioUmEmail, String usuarioDoisEmail, Long idGasto, BigDecimal valorDividido ){
        String token = tokenService.getCurrentToken();
        GastoDTO gasto = gastoClient.gastoExiste(idGasto, token);

        gastoClient.buscarUsuarioPorEmail(usuarioDoisEmail, token);

        if (gasto == null) {
            throw new RuntimeException("Gasto não encontrado");
        }

        if (pendenteRepository.existsByIdGasto(idGasto)) {
            throw new RuntimeException("Já existe uma pendência para este gasto");
        }

        Pendente pendente = Pendente.builder()
                .descricao(gasto.getDescricao())
                .valorTotal(gasto.getValorTotal())
                .valorDividido(valorDividido)
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
                .valorDividido(pendente.getValorDividido())
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

    public DivisaoResponseDTO aceitaDivisao(Long id) {
        Pendente pendencia = pendenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pendencia não encontrada"));

        String token = tokenService.getCurrentToken();

        GastoDTO gasto = gastoClient.gastoExiste(pendencia.getIdGasto(), token);

        BigDecimal valor = pendencia.getValorTotal().subtract(pendencia.getValorDividido());
        if (valor.compareTo(BigDecimal.ZERO) > 0) {
//            Fazer update no valor do gasto e não criar um novo (pegar pelo idGasto da pendencia)
            GastoDTO gastoUm = GastoDTO.builder()
                    .descricao(pendencia.getDescricao())
                    .valorTotal(valor)
                    .data(pendencia.getData())
                    .parcelado(false)
                    .numeroParcelas(1)
                    .usuarioId(pendencia.getUsuarioUmId())
                    .fonte(pendencia.getFonte())
                    .categoriaId(pendencia.getCategoriaId())
                    .build();
            GastoDTO gastoDividido = gastoClient.divideGastos(gastoUm, token);
        }

        CategoriaDTO buscaCategoria = gastoClient.buscarCategoriaPorNome("Divisao", token);
        CategoriaDTO novaCategoria;
        if(buscaCategoria == null){
            // Criar categoria de divisao para o usuario2
            CategoriaDTO categoria = CategoriaDTO.builder()
                    .nome("Divisao")
                    .descricao("Divisao de gastos")
                    .usuarioId(pendencia.getUsuarioDoisId())
                    .build();
            novaCategoria = gastoClient.criarCategoria(categoria, token);
        }else{
            novaCategoria = buscaCategoria;
        }

        GastoDTO gastoDois = GastoDTO.builder()
                .descricao(pendencia.getDescricao())
                .valorTotal(pendencia.getValorDividido())
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
                .gastoUsuarioDois(gastoDivididoDois)
                .build();
    }

    public void recusaDivisao(Long id) {
        pendenteRepository.deleteById(id);
    }
}
