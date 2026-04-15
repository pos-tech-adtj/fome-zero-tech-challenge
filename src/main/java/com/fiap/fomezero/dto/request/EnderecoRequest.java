package com.fiap.fomezero.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoRequest {

    @NotBlank
    private String rua;

    @NotBlank
    private String numero;

    private String complemento;

    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    @NotBlank
    private String cep;
}
