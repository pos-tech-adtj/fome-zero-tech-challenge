package com.fiap.fomezero.dto.response;

import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private boolean autenticado;

    public static LoginResponse from(Usuario user) {
        return LoginResponse.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .tipoUsuario(user.getTipoUsuario())
                .autenticado(true)
                .build();
    }
}
