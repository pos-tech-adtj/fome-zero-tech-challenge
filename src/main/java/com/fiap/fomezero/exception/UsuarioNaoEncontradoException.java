package com.fiap.fomezero.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    private static String message = "Nenhum usuário encontrado com o id informado";

    public UsuarioNaoEncontradoException() {
        super(message);
    }
}
