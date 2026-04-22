package com.fiap.fomezero.service;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.dto.request.LoginRequest;
import com.fiap.fomezero.dto.response.LoginResponse;
import com.fiap.fomezero.exception.CredenciaisInvalidasException;
import com.fiap.fomezero.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public LoginResponse autenticarUsuario(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByLogin(request.login())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getSenha().equals(request.senha())) {
            throw new CredenciaisInvalidasException();
        }

        return LoginResponse.from(usuario);
    }
}
