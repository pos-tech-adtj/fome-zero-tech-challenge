package com.fiap.fomezero.dto.request;

import com.fiap.fomezero.domain.model.TipoUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioCreateRequest {

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;

    @NotBlank
    private String senha;

    @NotNull
    private TipoUsuario tipoUsuario;

    @Valid
    private EnderecoRequest endereco;
}
