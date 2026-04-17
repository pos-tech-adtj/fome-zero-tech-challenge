package com.postech.restaurante.service;

import com.postech.restaurante.dto.request.*;
import com.postech.restaurante.dto.response.*;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse criar(CriarUsuarioRequest request);

    UsuarioResponse buscarPorId(Long id);

    List<UsuarioResponse> buscarPorNome(String nome);

    /**
     * Busca usuários cujo nome contenha o termo informado (case-insensitive).
     * Lança UsuarioNotFoundException → 404 Not Found se nenhum resultado.
     * Endpoint: GET /api/v1/usuarios/{nome}
     */
    List<UsuarioResponse> buscarPorNomeOuFalhar(String nome);

    List<UsuarioResponse> listarTodos();

    UsuarioResponse atualizar(Long id, AtualizarUsuarioRequest request);

    void trocarSenha(Long id, TrocarSenhaRequest request);

    void deletar(Long id);

    LoginResponse validarLogin(LoginRequest request);
}
