package com.example.funcionarios_service.service;

import com.example.funcionarios_service.client.SetorClient;
import com.example.funcionarios_service.dto.FuncionarioRequestDTO;
import com.example.funcionarios_service.dto.FuncionarioResponseDTO;
import com.example.funcionarios_service.dto.SalariosPorSetorDTO;
import com.example.funcionarios_service.dto.SetorDTO;
import com.example.funcionarios_service.entity.Funcionario;
import com.example.funcionarios_service.exception.FuncionarioNotFoundException;
import com.example.funcionarios_service.exception.SetorNotFoundException;
import com.example.funcionarios_service.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final TokenService tokenService;
    private final SetorClient setorClient;

    public FuncionarioResponseDTO criarFuncionario(String usuarioEmail, FuncionarioRequestDTO dto) {
        String token = tokenService.getCurrentToken();
        try {
            if (!setorClient.setorExiste(dto.getSetorId(), token)) {
                throw new SetorNotFoundException("Categoria não encontrada");
            }

        } catch (RuntimeException e) {
            throw new SetorNotFoundException("Não é possível criar o gasto no momento. Serviço de categorias indisponível.");

        }

        Funcionario funcionario = Funcionario.builder()
                .nome(dto.getNome())
                .cargo(dto.getCargo())
                .telefone(dto.getTelefone())
                .salario(dto.getSalario())
                .ativo(true)
                .setorId(dto.getSetorId())
                .usuarioId(usuarioEmail)
                .dataCadastro(dto.getDataCadastro())
                .build();

        funcionario = funcionarioRepository.save(funcionario);

        return mapToResponseDTO(funcionario);
    }


    public List<FuncionarioResponseDTO> listarPorUsuarioComFiltroAtivo(String usuarioId, Boolean ativo) {
        return funcionarioRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(func -> ativo == null || func.isAtivo() == ativo)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public FuncionarioResponseDTO alterarStatusAtivo(Long funcionarioId, boolean novoStatus, String emailUsuario) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new FuncionarioNotFoundException("Funcionário não encontrado"));

        if (!funcionario.getUsuarioId().equals(emailUsuario)) {
            throw new RuntimeException("Você não tem permissão para alterar este funcionario.");
        }

        funcionario.setAtivo(novoStatus); //mudança interna, não no banco de dados
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        return mapToResponseDTO(funcionarioSalvo);
    }

    public void desativarFuncionarios(List<Long> funcionariosIds, String emailUsuario) {
        if (funcionariosIds == null || funcionariosIds.isEmpty()) {
            throw new IllegalArgumentException("Lista de IDs não pode estar vazia");
        }
        List<Funcionario> funcionarios = funcionarioRepository.findAllById(funcionariosIds);
        if (funcionarios.size() != funcionariosIds.size()) {
            throw new FuncionarioNotFoundException("Um ou mais funcionarios não foram encontrados");
        }
        List<Funcionario> funcionariosNaoAutorizados = funcionarios.stream()
                .filter(funcionario -> !funcionario.getUsuarioId().equals(emailUsuario))
                .collect(Collectors.toList());
        if (!funcionariosNaoAutorizados.isEmpty()) {
            throw new RuntimeException("Você não tem permissão para desativar um ou mais funcionarios");
        }
        funcionarios.forEach(funcionario -> funcionario.setAtivo(false));
        funcionarioRepository.saveAll(funcionarios);
    }

    public FuncionarioResponseDTO editarFuncionario(Long funcionarioId, FuncionarioRequestDTO dto, String emailUsuario) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new FuncionarioNotFoundException("Funcionário não encontrado"));

        if (!funcionario.getUsuarioId().equals(emailUsuario)) {
            throw new RuntimeException("Você não tem permissão para editar este funcionario.");
        }

        String token = tokenService.getCurrentToken();
       if (!setorClient.setorExiste(dto.getSetorId(), token)) {
          throw new SetorNotFoundException("Setor não encontrado");
      }
        funcionario.setNome(dto.getNome());
        funcionario.setTelefone(dto.getTelefone());
        funcionario.setCargo(dto.getCargo());
        funcionario.setSalario(dto.getSalario());
        funcionario.setSetorId(dto.getSetorId());
        funcionario.setDataCadastro(dto.getDataCadastro());

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        return mapToResponseDTO(funcionarioSalvo);
    }

    public BigDecimal calcularTotalSalariosMensal(String usuarioEmail) {
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuarioId(usuarioEmail);

        return funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean existeFuncionarioComSetor(Long setorId, String usuarioEmail) {
        return funcionarioRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .filter(Funcionario::isAtivo)
                .anyMatch(funcionario -> funcionario.getSetorId().equals(setorId));
    }

    public void excluirFuncionarios(List<Long> funcionariosIds, String emailUsuario) {
        if (funcionariosIds == null || funcionariosIds.isEmpty()) {
            throw new IllegalArgumentException("Lista de IDs não pode estar vazia");
        }
        List<Funcionario> funcionarios = funcionarioRepository.findAllById(funcionariosIds);
        if (funcionarios.size() != funcionariosIds.size()) {
            throw new FuncionarioNotFoundException("Um ou mais funcionarios não foram encontrados");
        }
        List<Funcionario> funcionariosNaoAutorizados = funcionarios.stream()
                .filter(funcionario -> !funcionario.getUsuarioId().equals(emailUsuario))
                .collect(Collectors.toList());
        if (!funcionariosNaoAutorizados.isEmpty()) {
            throw new RuntimeException("Você não tem permissão para excluir um ou mais funcionarios");
        }
        funcionarioRepository.deleteAll(funcionarios);
    }

    public List<SalariosPorSetorDTO> calcularTotalSalariosPorSetor(String usuarioEmail) {
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuarioId(usuarioEmail);

        Map<Long, BigDecimal> salariosPorSetor = funcionarios.stream()
                .filter(Funcionario::isAtivo)
                .collect(Collectors.groupingBy(
                        Funcionario::getSetorId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Funcionario::getSalario,
                                BigDecimal::add
                        )
                ));

        String token = tokenService.getCurrentToken();
        List<SalariosPorSetorDTO> resultado = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry : salariosPorSetor.entrySet()) {
            Long setorId = entry.getKey();
            BigDecimal totalSalarios = entry.getValue();

            SetorDTO setorDTO = setorClient.buscarSetorPorId(setorId, token);

            SalariosPorSetorDTO salarioSetor = SalariosPorSetorDTO.builder()
                    .setorId(setorId)
                    .setorNome(setorDTO != null ? setorDTO.getNome() : "Setor não encontrado")
                    .totalSalarios(totalSalarios)
                    .build();

            resultado.add(salarioSetor);
        }

        resultado.sort(Comparator.comparing(SalariosPorSetorDTO::getSetorNome));

        return resultado;
    }

    private FuncionarioResponseDTO mapToResponseDTO(Funcionario funcionario) {
      String token = tokenService.getCurrentToken();
      SetorDTO setorDTO = setorClient.buscarSetorPorId(funcionario.getSetorId(), token);

        return FuncionarioResponseDTO.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .cargo(funcionario.getCargo())
                .telefone(funcionario.getTelefone())
                .dataCadastro(funcionario.getDataCadastro())
                .salario(funcionario.getSalario())
                .ativo(funcionario.isAtivo())
                .usuarioId(funcionario.getUsuarioId())
                .setor(setorDTO)
                .build();
    }
}
