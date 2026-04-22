package com.fiap.fomezero.service;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.dto.response.UsuarioResponse;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import com.fiap.fomezero.mapper.UsuarioMapper;
import com.fiap.fomezero.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse criarUsuario(UsuarioCreateRequest request) {

        validarEmailCadastrado(request.email());
        validarLoginCadastrado(request.login());

        Usuario usuario = UsuarioMapper.toEntity(request);
        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }

    public UsuarioResponse atualizarUsuario(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        validarInformacoesAtualizadas(usuario, request);

        UsuarioMapper.updateEntity(usuario, request);
        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }

    private void validarEmailCadastrado(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException();
        }
    }

    private void validarLoginCadastrado(String login) {
        if (usuarioRepository.existsByLogin(login)) {
            throw new LoginJaCadastradoException();
        }
    }

    private void validarInformacoesAtualizadas(Usuario usuario, UsuarioUpdateRequest request) {
        if (request.email() != null && !request.email().equals(usuario.getEmail())) {
            validarEmailCadastrado(request.email());
        }
        if (request.login() != null &&!request.login().equals(usuario.getLogin())) {
            validarLoginCadastrado(request.login());
        }
    }

    public UsuarioResponse buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        return UsuarioMapper.toResponse(usuario);
    }

    public List<UsuarioResponse> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(UsuarioMapper::toResponse).toList();
    }
}
