package com.example.gastos_service.service;

import com.example.gastos_service.dto.GastoRequestDTO;
import com.example.gastos_service.dto.GastoResponseDTO;
import com.example.gastos_service.dto.ParcelaDTO;
import com.example.gastos_service.entity.Gasto;
import com.example.gastos_service.entity.Parcela;
import com.example.gastos_service.repository.GastoRepository;
import com.example.gastos_service.repository.ParcelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final ParcelaRepository parcelaRepository;

    public GastoResponseDTO criarGasto(String usuarioEmail, GastoRequestDTO dto) {
        Gasto gasto = Gasto.builder()
                .descricao(dto.getDescricao())
                .valorTotal(dto.getValorTotal())
                .data(dto.getData())
                .parcelado(dto.isParcelado())
                .numeroParcelas(dto.getNumeroParcelas())
                .usuarioId(usuarioEmail)
                .fonte(dto.getFonte())
                .categoria(dto.getCategoria())
                .build();

        // Se for parcelado, cria parcelas
        if (dto.isParcelado() && dto.getNumeroParcelas() != null && dto.getNumeroParcelas() > 1) {
            List<Parcela> parcelas = new ArrayList<>();
            BigDecimal valorParcela = dto.getValorTotal()
                    .divide(BigDecimal.valueOf(dto.getNumeroParcelas()), 2, BigDecimal.ROUND_HALF_UP);

            for (int i = 0; i < dto.getNumeroParcelas(); i++) {
                Parcela parcela = Parcela.builder()
                        .numeroParcela(i + 1)
                        .valorParcela(valorParcela)
                        .dataVencimento(dto.getData().plusMonths(i))
                        .gasto(gasto)
                        .build();
                parcelas.add(parcela);
            }

            gasto.setParcelas(parcelas);
        }

        Gasto salvo = gastoRepository.save(gasto);

        return mapToResponseDTO(salvo);
    }

    public List<GastoResponseDTO> listarPorUsuario(String usuarioEmail) {
        return gastoRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private GastoResponseDTO mapToResponseDTO(Gasto gasto) {
        List<ParcelaDTO> parcelas = new ArrayList<>();
        if (gasto.isParcelado() && gasto.getParcelas() != null) {
            parcelas = gasto.getParcelas().stream().map(p -> ParcelaDTO.builder()
                    .id(p.getId())
                    .numeroParcela(p.getNumeroParcela())
                    .valorParcela(p.getValorParcela())
                    .dataVencimento(p.getDataVencimento())
                    .build()).collect(Collectors.toList());
        }

        return GastoResponseDTO.builder()
                .id(gasto.getId())
                .descricao(gasto.getDescricao())
                .valorTotal(gasto.getValorTotal())
                .data(gasto.getData())
                .parcelado(gasto.isParcelado())
                .numeroParcelas(gasto.getNumeroParcelas())
                .usuarioId(gasto.getUsuarioId())
                .parcelas(parcelas)
                .fonte(gasto.getFonte())
                .categoria(gasto.getCategoria())
                .build();
    }
}
