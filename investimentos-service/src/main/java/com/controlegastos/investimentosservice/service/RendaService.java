package com.controlegastos.investimentosservice.service;

import com.controlegastos.investimentosservice.dto.RendaRequestDTO;
import com.controlegastos.investimentosservice.dto.RendaResponseDTO;
import com.controlegastos.investimentosservice.entity.Investimento;
import com.controlegastos.investimentosservice.entity.Renda;
import com.controlegastos.investimentosservice.repository.InvestimentoRepository;
import com.controlegastos.investimentosservice.repository.RendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RendaService {
    private final RendaRepository repository;
    private final InvestimentoRepository investimentoRepository;

    public RendaResponseDTO criarRenda(String usuarioEmail, RendaRequestDTO dto) {
        Investimento investimento = investimentoRepository.findByIdAndUsuarioId(dto.getInvestimentoId(), usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));

        Renda renda = Renda.builder()
                .titulo(dto.getTitulo())
                .valor(dto.getValor())
                .data(dto.getData())
                .usuarioId(usuarioEmail)
                .investimento(investimento)
                .build();

        renda = repository.save(renda);
        return toResponseDTO(renda);
    }

    public List<RendaResponseDTO> listarRendasPorUsuario(String usuarioEmail) {
        return repository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RendaResponseDTO> listarRendasPorInvestimento(UUID investimentoId, String usuarioEmail) {
        investimentoRepository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));

        return repository.findByInvestimentoIdAndUsuarioId(investimentoId, usuarioEmail)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RendaResponseDTO buscarPorId(UUID id, String usuarioEmail) {
        Renda renda = repository.findByIdAndUsuarioId(id, usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Renda não encontrada"));
        return toResponseDTO(renda);
    }

    public void deletar(UUID id, String usuarioEmail) {
        Renda renda = repository.findByIdAndUsuarioId(id, usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Renda não encontrada"));
        repository.deleteById(id);
    }

    private RendaResponseDTO toResponseDTO(Renda renda) {
        return RendaResponseDTO.builder()
                .id(renda.getId())
                .titulo(renda.getTitulo())
                .valor(renda.getValor())
                .data(renda.getData())
                .usuarioId(renda.getUsuarioId())
                .investimentoId(renda.getInvestimento().getId())
                .build();
    }
}