package com.controlegastos.gastosservice.service;

import com.controlegastos.gastosservice.dto.ParcelaDTO;
import com.controlegastos.gastosservice.entity.Parcela;
import com.controlegastos.gastosservice.repository.ParcelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelaService {

    private final ParcelaRepository parcelaRepository;

    public List<ParcelaDTO> listarParcelasDoUsuario(String usuarioEmail) {
        List<Parcela> parcelas = parcelaRepository.findAllByGasto_UsuarioId(usuarioEmail);
        return parcelas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ParcelaDTO mapToDTO(Parcela parcela) {
        return ParcelaDTO.builder()
                .id(parcela.getId())
                .numeroParcela(parcela.getNumeroParcela())
                .valorParcela(parcela.getValorParcela())
                .dataVencimento(parcela.getDataVencimento())
                .build();
    }
}

