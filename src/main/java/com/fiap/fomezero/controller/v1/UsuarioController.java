package com.fiap.fomezero.controller.v1;

import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.response.UsuarioResponse;
import com.fiap.fomezero.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody UsuarioCreateRequest request) {

        UsuarioResponse usuario = usuarioService.criarUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
