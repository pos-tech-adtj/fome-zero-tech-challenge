package com.fiap.fomezero.service;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.response.UsuarioResponse;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.mapper.UsuarioMapper;
import com.fiap.fomezero.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse criarUsuario(UsuarioCreateRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailJaCadastradoException();
        }

        Usuario usuario = UsuarioMapper.toEntity(request);
        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }
}
