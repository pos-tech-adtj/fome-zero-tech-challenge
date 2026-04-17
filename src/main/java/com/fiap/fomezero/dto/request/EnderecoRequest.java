package com.fiap.fomezero.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EnderecoRequest(

        @NotBlank
        String rua,

        @NotNull
        @Positive
        Integer numero,

        String complemento,

        String bairro,

        @NotBlank
        String cidade,

        @NotBlank
        String estado,

        @NotBlank
        String cep

) {}