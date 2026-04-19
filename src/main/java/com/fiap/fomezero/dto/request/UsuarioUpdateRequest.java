package com.fiap.fomezero.dto.request;

import com.fiap.fomezero.domain.model.TipoUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public record UsuarioUpdateRequest(

        String nome,

        @Email
        String email,

        String login,

        TipoUsuario tipoUsuario,

        @Valid
        EnderecoRequest endereco
) {
}