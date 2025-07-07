package com.controlegastos.investimentosservice.service;

import com.controlegastos.investimentosservice.dto.CompraInvestimentoRequestDTO;
import com.controlegastos.investimentosservice.dto.CompraInvestimentoResponseDTO;
import com.controlegastos.investimentosservice.entity.CompraInvestimento;
import com.controlegastos.investimentosservice.entity.Investimento;
import com.controlegastos.investimentosservice.exception.CompraInvestimentoNotFoundException;
import com.controlegastos.investimentosservice.exception.InvestimentoNotFoundException;
import com.controlegastos.investimentosservice.repository.CompraInvestimentoRepository;
import com.controlegastos.investimentosservice.repository.InvestimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraInvestimentoService {
    private final CompraInvestimentoRepository repository;
    private final InvestimentoRepository investimentoRepository;

    public CompraInvestimentoResponseDTO criarCompra(String usuarioEmail, CompraInvestimentoRequestDTO dto) {
        Investimento investimento = investimentoRepository.findByIdAndUsuarioId(dto.getInvestimentoId(), usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));

        CompraInvestimento compra = CompraInvestimento.builder()
                .valor(dto.getValor())
                .data(dto.getData())
                .usuarioId(usuarioEmail)
                .investimento(investimento)
                .build();

        compra = repository.save(compra);
        return toResponseDTO(compra);
    }

    public List<CompraInvestimentoResponseDTO> listarComprasPorUsuario(String usuarioEmail) {
        return repository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CompraInvestimentoResponseDTO> listarComprasPorInvestimento(UUID investimentoId, String usuarioEmail) {
        investimentoRepository.findByIdAndUsuarioId(investimentoId, usuarioEmail)
                .orElseThrow(() -> new InvestimentoNotFoundException("Investimento não encontrado"));

        return repository.findByInvestimentoIdAndUsuarioId(investimentoId, usuarioEmail)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CompraInvestimentoResponseDTO buscarPorId(UUID id, String usuarioEmail) {
        CompraInvestimento compra = repository.findByIdAndUsuarioId(id, usuarioEmail)
                .orElseThrow(() -> new CompraInvestimentoNotFoundException("Compra não encontrada"));
        return toResponseDTO(compra);
    }

    public void deletar(UUID id, String usuarioEmail) {
        CompraInvestimento compra = repository.findByIdAndUsuarioId(id, usuarioEmail)
                .orElseThrow(() -> new CompraInvestimentoNotFoundException("Compra não encontrada"));
        repository.deleteById(id);
    }

    private CompraInvestimentoResponseDTO toResponseDTO(CompraInvestimento compra) {
        return CompraInvestimentoResponseDTO.builder()
                .id(compra.getId())
                .valor(compra.getValor())
                .data(compra.getData())
                .usuarioId(compra.getUsuarioId())
                .investimentoId(compra.getInvestimento().getId())
                .build();
    }
}