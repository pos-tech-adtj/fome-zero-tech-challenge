package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.response.EnderecoResponse;
import com.fiap.fomezero.dto.response.UsuarioResponse;

import java.time.LocalDateTime;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioCreateRequest request) {

        Endereco endereco = null;

        if (request.getEndereco() != null) {
            endereco = Endereco.builder()
                    .rua(request.getEndereco().getRua())
                    .numero(request.getEndereco().getNumero())
                    .complemento(request.getEndereco().getComplemento())
                    .bairro(request.getEndereco().getBairro())
                    .cidade(request.getEndereco().getCidade())
                    .estado(request.getEndereco().getEstado())
                    .cep(request.getEndereco().getCep())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .login(request.getLogin())
                .senha(request.getSenha())
                .tipoUsuario(request.getTipoUsuario())
                .dataUltimaAlteracaoSenha(LocalDateTime.now())
                .endereco(endereco)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static UsuarioResponse toResponse(Usuario usuario) {

        EnderecoResponse enderecoResponse = null;

        if (usuario.getEndereco() != null) {
            enderecoResponse = EnderecoResponse.builder()
                    .rua(usuario.getEndereco().getRua())
                    .numero(usuario.getEndereco().getNumero())
                    .complemento(usuario.getEndereco().getComplemento())
                    .bairro(usuario.getEndereco().getBairro())
                    .cidade(usuario.getEndereco().getCidade())
                    .estado(usuario.getEndereco().getEstado())
                    .cep(usuario.getEndereco().getCep())
                    .build();
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .tipoUsuario(usuario.getTipoUsuario())
                .endereco(enderecoResponse)
                .build();
    }
}
