package com.example.divisao_service.service;

import com.example.divisao_service.dto.PendenteRequestDTO;
import com.example.divisao_service.dto.PendenteResponseDTO;
import com.example.divisao_service.entity.Pendente;
import com.example.divisao_service.repository.PendenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendenteService {

    private final PendenteRepository pendenteRepository;

    public PendenteResponseDTO salvarPendente(String usuarioUmEmail, String usuarioDoisEmail, PendenteRequestDTO dto){
        Pendente pendente = Pendente.builder()
                .descricao(dto.getDescricao())
                .valorTotal(dto.getValorTotal())
                .data(dto.getData())
                .numeroParcelas(dto.getNumeroParcelas())
                .usuarioUmId(usuarioUmEmail) // <- associa o usuário que criou o gasto
                .usuarioDoisId(usuarioDoisEmail) // <- associa ao usuario que dividira o gasto
                .fonte(dto.getFonte())
                .categoria(dto.getCategoria())
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
                .fonte(pendente.getFonte())
                .categoria(pendente.getCategoria())
                .build();
    }

    public List<PendenteResponseDTO> listarPorUsuarioDois(String usuarioEmail) {
        return pendenteRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void divideGastos(){
//        Este metodo ira receber o id/ou objeto da tabela pendente, salvar na tabela de gastos 2x
//        uma com o id do usuario1 (criou o gasto) e outra com id do usuario2, apos isso deletar da tabela pendente
//
    }

}
