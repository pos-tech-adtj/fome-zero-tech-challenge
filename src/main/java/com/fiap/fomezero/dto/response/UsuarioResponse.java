package com.fiap.fomezero.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.fomezero.domain.model.TipoUsuario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private String login;
    private TipoUsuario tipoUsuario;

    private EnderecoResponse endereco;
}
