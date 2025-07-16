package com.example.setor_service.service;

import com.example.setor_service.client.FuncionarioClient;
import com.example.setor_service.dto.SetorRequestDTO;
import com.example.setor_service.dto.SetorResponseDTO;
import com.example.setor_service.entity.Setor;
import com.example.setor_service.exception.SetorMembershipException;
import com.example.setor_service.exception.SetorNotBelongToUser;
import com.example.setor_service.exception.SetorNotFoundException;
import com.example.setor_service.repository.SetorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SetorService {

    private final SetorRepository setorRepository;
    private final TokenService tokenService;
    private final FuncionarioClient funcionarioClient;

    public SetorResponseDTO criarSetor(String usuarioEmail, SetorRequestDTO dto) {
        if (setorRepository.existsByNomeAndUsuarioId(dto.getNome(), usuarioEmail)) {
            throw new RuntimeException("Já existe um setor com esse nome para o usuário");
        }

        Setor setor = Setor.builder()
                .nome(dto.getNome())
                .usuarioId(usuarioEmail)
                .build();

        setor = setorRepository.save(setor);
        return toResponseDTO(setor);
    }

    public List<SetorResponseDTO> listarSetoresPorUsuario(String usuarioEmail) {
        return setorRepository.findByUsuarioId(usuarioEmail)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SetorResponseDTO buscarPorId(Long id, String usuarioEmail) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(() -> new SetorNotFoundException("Setor não encontrado"));

        if (!setor.getUsuarioId().equals(usuarioEmail)) {
            throw new SetorNotBelongToUser("Setor não pertence ao usuário");
        }

        return toResponseDTO(setor);
    }

    public SetorResponseDTO buscarPorNome(String nome, String usuarioEmail){
        Setor setor = setorRepository.findByNomeAndUsuarioId(nome, usuarioEmail)
                .orElseThrow(() -> new SetorNotFoundException("Setor não encontrado"));
        return toResponseDTO(setor);
    }

    public SetorResponseDTO atualizarSetor(Long id, String usuarioEmail, SetorRequestDTO dto) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(() -> new SetorNotFoundException("Setor não encontrado"));

        if (!setor.getUsuarioId().equals(usuarioEmail)) {
            throw new RuntimeException("Setor não pertence ao usuário");
        }

        setor.setNome(dto.getNome());
        setor = setorRepository.save(setor);

        return toResponseDTO(setor);
    }

    public void deletar(Long id, String usuarioEmail) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(() -> new SetorNotFoundException("Setor não encontrado"));

        if (!setor.getUsuarioId().equals(usuarioEmail)) {
            throw new SetorNotBelongToUser("Setor não pertence ao usuário");
        }

        String token = tokenService.getCurrentToken();
        if (funcionarioClient.existeFuncionarioComSetor(id, token)) {
            throw new SetorMembershipException("Não é possível excluir o setor pois existem funcionário associados a ele");
        }

        setorRepository.deleteById(id);
    }


    private SetorResponseDTO toResponseDTO(Setor setor) {
        return SetorResponseDTO.builder()
                .id(setor.getId())
                .nome(setor.getNome())
                .usuarioId(setor.getUsuarioId())
                .build();
    }

}
