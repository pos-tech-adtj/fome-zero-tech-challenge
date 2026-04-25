package com.fiap.fomezero.controller.v1;

import com.fiap.fomezero.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.dto.response.UsuarioResponse;
import com.fiap.fomezero.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

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
  
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {

        UsuarioResponse usuario = usuarioService.atualizarUsuario(id, request);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {

        UsuarioResponse usuario = usuarioService.buscarUsuarioPorId(id);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {

        List<UsuarioResponse> usuarios = usuarioService.listarTodosUsuarios();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<UsuarioResponse>> buscarUsuariosPorNome(@PathVariable String nome) {

        List<UsuarioResponse> usuarios = usuarioService.buscarUsuariosPorNome(nome);

        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaRequest request) {
        usuarioService.alterarSenha(id, request);
        return ResponseEntity.noContent().build();
    }
}
