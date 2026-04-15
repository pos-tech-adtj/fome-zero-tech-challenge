package com.fiap.fomezero.dto.request;

import com.fiap.fomezero.domain.model.TipoUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioCreateRequest(

        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String login,

        @NotBlank
        String senha,

        @NotNull
        TipoUsuario tipoUsuario,

        @Valid
        EnderecoRequest endereco
) {
}