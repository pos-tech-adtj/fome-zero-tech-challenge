package com.fiap.fomezero.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRequest(

        @NotBlank
        String rua,

        @NotBlank
        String numero,

        String complemento,

        String bairro,

        @NotBlank
        String cidade,

        @NotBlank
        String estado,

        @NotBlank
        String cep

) {}