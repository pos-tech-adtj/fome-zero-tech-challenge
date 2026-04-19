package com.fiap.fomezero.service;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.response.UsuarioResponse;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import com.fiap.fomezero.mapper.UsuarioMapper;
import com.fiap.fomezero.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse criarUsuario(UsuarioCreateRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException();
        }

        if (usuarioRepository.existsByLogin(request.login())) {
            throw new LoginJaCadastradoException();
        }

        Usuario usuario = UsuarioMapper.toEntity(request);
        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException();
        }
        usuarioRepository.deleteById(id);
    }
}
