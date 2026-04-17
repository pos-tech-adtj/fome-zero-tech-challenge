package com.postech.restaurante.service.impl;

import com.postech.restaurante.dto.request.*;
import com.postech.restaurante.dto.response.*;
import com.postech.restaurante.entity.Endereco;
import com.postech.restaurante.entity.Usuario;
import com.postech.restaurante.exception.*;
import com.postech.restaurante.repository.UsuarioRepository;
import com.postech.restaurante.service.UsuarioService;
import com.postech.restaurante.util.PasswordUtils;
import com.postech.restaurante.util.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordUtils passwordUtils;

    @Override
    @Transactional
    public UsuarioResponse criar(CriarUsuarioRequest request) {
        log.info("Criando usuário com email: {}", request.getEmail());

        if (usuarioRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new EmailJaCadastradoException(request.getEmail());
        }
        if (usuarioRepository.existsByLogin(request.getLogin().toLowerCase().trim())) {
            throw new LoginJaCadastradoException(request.getLogin());
        }

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setSenha(passwordUtils.hash(request.getSenha()));
        usuario = usuarioRepository.save(usuario);

        Endereco endereco = usuarioMapper.toEnderecoEntity(request.getEndereco(), usuario);
        usuario.setEndereco(endereco);
        usuario = usuarioRepository.save(usuario);

        log.info("Usuário criado com id: {}", usuario.getId());
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return usuarioMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorNome(String nome) {
        log.info("Buscando usuários pelo nome (query param): {}", nome);
        return usuarioRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    // ── TC-3: GET /api/v1/usuarios/{nome} ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorNomeOuFalhar(String nome) {
        log.info("Buscando usuários pelo nome (path variable): {}", nome);

        List<UsuarioResponse> resultado = usuarioRepository
                .findByNomeContainingIgnoreCase(nome.trim())
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();

        if (resultado.isEmpty()) {
            throw new UsuarioNotFoundException(
                    "Nenhum usuário encontrado com o nome: " + nome);
        }

        return resultado;
    }
    // ─────────────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponse atualizar(Long id, AtualizarUsuarioRequest request) {
        log.info("Atualizando dados do usuário id: {}", id);
        Usuario usuario = findByIdOrThrow(id);

        String novoEmail = request.getEmail().toLowerCase().trim();
        if (usuarioRepository.existsByEmailAndIdNot(novoEmail, id)) {
            throw new EmailJaCadastradoException(request.getEmail());
        }

        String novoLogin = request.getLogin().toLowerCase().trim();
        if (usuarioRepository.existsByLoginAndIdNot(novoLogin, id)) {
            throw new LoginJaCadastradoException(request.getLogin());
        }

        usuario.setNome(request.getNome());
        usuario.setEmail(novoEmail);
        usuario.setLogin(novoLogin);

        if (request.getEndereco() != null) {
            Endereco endereco = usuario.getEndereco();
            if (endereco == null) {
                endereco = usuarioMapper.toEnderecoEntity(request.getEndereco(), usuario);
                usuario.setEndereco(endereco);
            } else {
                EnderecoRequest er = request.getEndereco();
                endereco.setRua(er.getRua());
                endereco.setNumero(er.getNumero());
                endereco.setComplemento(er.getComplemento());
                endereco.setBairro(er.getBairro());
                endereco.setCidade(er.getCidade());
                endereco.setEstado(er.getEstado().toUpperCase());
                endereco.setCep(er.getCep());
            }
        }

        usuario = usuarioRepository.save(usuario);
        log.info("Usuário id {} atualizado com sucesso", id);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public void trocarSenha(Long id, TrocarSenhaRequest request) {
        log.info("Trocando senha do usuário id: {}", id);
        Usuario usuario = findByIdOrThrow(id);

        if (!passwordUtils.matches(request.getSenhaAtual(), usuario.getSenha())) {
            throw new SenhaInvalidaException("Senha atual incorreta.");
        }
        if (!request.getNovaSenha().equals(request.getConfirmarSenha())) {
            throw new SenhaInvalidaException("Nova senha e confirmação não conferem.");
        }
        if (request.getSenhaAtual().equals(request.getNovaSenha())) {
            throw new SenhaInvalidaException("A nova senha deve ser diferente da senha atual.");
        }

        usuario.setSenha(passwordUtils.hash(request.getNovaSenha()));
        usuarioRepository.save(usuario);
        log.info("Senha do usuário id {} alterada com sucesso", id);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando usuário id: {}", id);
        usuarioRepository.delete(findByIdOrThrow(id));
        log.info("Usuário id {} deletado com sucesso", id);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse validarLogin(LoginRequest request) {
        log.info("Validando login para: {}", request.getLogin());

        return usuarioRepository.findByLogin(request.getLogin().toLowerCase().trim())
                .filter(u -> passwordUtils.matches(request.getSenha(), u.getSenha()))
                .map(u -> LoginResponse.builder()
                        .autenticado(true)
                        .mensagem("Login efetuado com sucesso.")
                        .usuarioId(u.getId())
                        .nome(u.getNome())
                        .tipoUsuario(u.getTipoUsuario())
                        .build())
                .orElse(LoginResponse.builder()
                        .autenticado(false)
                        .mensagem("Login ou senha inválidos.")
                        .build());
    }

    // ── helpers ───────────────────────────────────────────────────────────────────

    private Usuario findByIdOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }
}
