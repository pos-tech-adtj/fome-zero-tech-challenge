package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.dto.response.EnderecoResponse;
import com.fiap.fomezero.dto.response.UsuarioResponse;

import java.time.LocalDateTime;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioCreateRequest request) {

        Endereco endereco = null;

        if (request.endereco() != null) {
            endereco = Endereco.builder()
                    .rua(request.endereco().rua())
                    .numero(request.endereco().numero())
                    .complemento(request.endereco().complemento())
                    .bairro(request.endereco().bairro())
                    .cidade(request.endereco().cidade())
                    .estado(request.endereco().estado())
                    .cep(request.endereco().cep())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        return Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .login(request.login())
                .senha(request.senha())
                .tipoUsuario(request.tipoUsuario())
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
                    .numero(String.valueOf(usuario.getEndereco().getNumero()))
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

    public static void updateEntity(Usuario usuario, UsuarioUpdateRequest request) {
        if (request.nome() != null) {
            usuario.setNome(request.nome());
        }
        if (request.email() != null) {
            usuario.setEmail(request.email());
        }
        if (request.login() != null) {
            usuario.setLogin(request.login());
        }
        if (request.tipoUsuario() != null) {
            usuario.setTipoUsuario(request.tipoUsuario());
        }
        if (request.endereco() != null) {
            Endereco endereco = usuario.getEndereco();
            if (endereco == null) {
                usuario.setEndereco(new Endereco());
            }
            if (request.endereco().rua() != null) {
                endereco.setRua(request.endereco().rua());
            }
            if (request.endereco().numero() != null) {
                endereco.setNumero(request.endereco().numero());
            }
            if (request.endereco().complemento() != null) {
                endereco.setComplemento(request.endereco().complemento());
            }
            if (request.endereco().bairro() != null) {
                endereco.setBairro(request.endereco().bairro());
            }
            if (request.endereco().cidade() != null) {
                endereco.setCidade(request.endereco().cidade());
            }
            if (request.endereco().estado() != null) {
                endereco.setEstado(request.endereco().estado());
            }
            if (request.endereco().cep() != null) {
                endereco.setCep(request.endereco().cep());
            }

            endereco.setUpdatedAt(LocalDateTime.now());
        }

        usuario.setUpdatedAt(LocalDateTime.now());
    }
}
