package com.fiap.fomezero.controller.v1;

import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.dto.response.UsuarioResponse;
import com.fiap.fomezero.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {

        UsuarioResponse usuario = usuarioService.atualizarUsuario(id, request);

        return ResponseEntity.ok(usuario);
    }
}
