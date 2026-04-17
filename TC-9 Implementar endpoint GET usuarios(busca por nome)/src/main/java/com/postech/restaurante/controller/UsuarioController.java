package com.postech.restaurante.controller;

import com.postech.restaurante.dto.request.*;
import com.postech.restaurante.dto.response.*;
import com.postech.restaurante.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários (donos de restaurante e clientes)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ── POST /api/v1/usuarios ─────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Criar usuário",
            description = "Cadastra um novo usuário (DONO_RESTAURANTE ou CLIENTE)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
        @ApiResponse(responseCode = "409", description = "E-mail ou login já cadastrado"),
        @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody CriarUsuarioRequest request) {
        UsuarioResponse response = usuarioService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    // ── GET /api/v1/usuarios ──────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Listar usuários",
            description = "Lista todos ou filtra por nome via query param (?nome=)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<UsuarioResponse>> listar(
            @Parameter(description = "Filtro parcial por nome (case-insensitive)")
            @RequestParam(required = false) String nome) {

        List<UsuarioResponse> lista = (nome != null && !nome.isBlank())
                ? usuarioService.buscarPorNome(nome)
                : usuarioService.listarTodos();

        return ResponseEntity.ok(lista);
    }

    // ── GET /api/v1/usuarios/nome/{nome} ──────────────────────────────────────────
    // TC-3: retorna 200 OK com lista ou 404 Not Found

    @GetMapping("/nome/{nome}")
    @Operation(
        summary = "Buscar usuário pelo nome",
        description = "Busca usuários cujo nome contenha o termo informado (case-insensitive). "
                    + "Retorna 200 OK se encontrar, 404 Not Found se não houver resultados."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuários encontrados",
            content = @Content(array = @ArraySchema(
                schema = @Schema(implementation = UsuarioResponse.class)))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Nenhum usuário encontrado com o nome informado"
        )
    })
    public ResponseEntity<List<UsuarioResponse>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome", example = "João")
            @PathVariable String nome) {

        return ResponseEntity.ok(usuarioService.buscarPorNomeOuFalhar(nome));
    }

    // ── GET /api/v1/usuarios/{id} ─────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado",
            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // ── PUT /api/v1/usuarios/{id} ─────────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do usuário",
            description = "Atualiza nome, e-mail, login e endereço. NÃO altera senha (use /senha)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "409", description = "E-mail ou login já em uso"),
        @ApiResponse(responseCode = "422", description = "Dados inválidos")
    })
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    // ── PATCH /api/v1/usuarios/{id}/senha ─────────────────────────────────────────

    @PatchMapping("/{id}/senha")
    @Operation(summary = "Trocar senha", description = "Endpoint exclusivo para troca de senha")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou confirmação inválida"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> trocarSenha(
            @PathVariable Long id,
            @Valid @RequestBody TrocarSenhaRequest request) {
        usuarioService.trocarSenha(id, request);
        return ResponseEntity.noContent().build();
    }

    // ── DELETE /api/v1/usuarios/{id} ──────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
