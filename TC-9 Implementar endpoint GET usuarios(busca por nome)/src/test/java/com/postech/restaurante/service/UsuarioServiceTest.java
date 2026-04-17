package com.postech.restaurante.service;

import com.postech.restaurante.dto.request.*;
import com.postech.restaurante.dto.response.*;
import com.postech.restaurante.entity.*;
import com.postech.restaurante.exception.*;
import com.postech.restaurante.repository.UsuarioRepository;
import com.postech.restaurante.service.impl.UsuarioServiceImpl;
import com.postech.restaurante.util.PasswordUtils;
import com.postech.restaurante.util.UsuarioMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Spy
    private UsuarioMapper usuarioMapper = new UsuarioMapper();

    @Spy
    private PasswordUtils passwordUtils = new PasswordUtils();

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioBase;
    private final PasswordUtils pw = new PasswordUtils();

    @BeforeEach
    void setUp() {
        Endereco endereco = Endereco.builder()
                .id(1L).rua("Rua das Flores").numero("100")
                .cidade("São Paulo").estado("SP").cep("01310-100").build();

        usuarioBase = Usuario.builder()
                .id(1L).nome("João Silva")
                .email("joao@email.com").login("joao")
                .senha(pw.hash("senha123"))
                .tipoUsuario(TipoUsuario.CLIENTE)
                .criadoEm(LocalDateTime.now()).ultimaAlteracao(LocalDateTime.now())
                .endereco(endereco).build();
        endereco.setUsuario(usuarioBase);
    }

    // ── criar ─────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuario() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);
        when(usuarioRepository.save(any())).thenReturn(usuarioBase);

        UsuarioResponse response = usuarioService.criar(buildCriarRequest());

        assertThat(response).isNotNull();
        assertThat(response.getNome()).isEqualTo("João Silva");
        verify(usuarioRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com e-mail duplicado")
    void deveLancarExcecaoEmailDuplicado() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.criar(buildCriarRequest()))
                .isInstanceOf(EmailJaCadastradoException.class);
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar id inexistente")
    void deveLancarNotFoundQuandoIdNaoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(99L))
                .isInstanceOf(UsuarioNotFoundException.class);
    }

    // ── buscarPorNomeOuFalhar — TC-3 ──────────────────────────────────────────────

    @Test
    @DisplayName("TC-3: Deve retornar 200 com lista quando nome existe")
    void deveBuscarPorNomeOuFalhar_encontrado() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("João"))
                .thenReturn(List.of(usuarioBase));

        List<UsuarioResponse> resultado = usuarioService.buscarPorNomeOuFalhar("João");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("João Silva");
        verify(usuarioRepository).findByNomeContainingIgnoreCase("João");
    }

    @Test
    @DisplayName("TC-3: Deve lançar UsuarioNotFoundException (404) quando nome não existe")
    void deveBuscarPorNomeOuFalhar_naoEncontrado() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("XYZ"))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> usuarioService.buscarPorNomeOuFalhar("XYZ"))
                .isInstanceOf(UsuarioNotFoundException.class)
                .hasMessageContaining("XYZ");
    }

    @Test
    @DisplayName("TC-3: Busca por nome deve ser case-insensitive")
    void deveBuscarPorNome_caseInsensitive() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("joão"))
                .thenReturn(List.of(usuarioBase));

        List<UsuarioResponse> resultado = usuarioService.buscarPorNomeOuFalhar("joão");

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getNome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("TC-3: Deve retornar múltiplos usuários com nome parcial")
    void deveBuscarPorNome_multiplosResultados() {
        Usuario usuarioExtra = Usuario.builder()
                .id(2L).nome("João Souza")
                .email("joao2@email.com").login("joao2")
                .senha(pw.hash("senha456"))
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .criadoEm(LocalDateTime.now()).ultimaAlteracao(LocalDateTime.now())
                .build();

        when(usuarioRepository.findByNomeContainingIgnoreCase("João"))
                .thenReturn(List.of(usuarioBase, usuarioExtra));

        List<UsuarioResponse> resultado = usuarioService.buscarPorNomeOuFalhar("João");

        assertThat(resultado).hasSize(2);
    }

    // ── trocarSenha ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve lançar exceção quando senha atual incorreta")
    void deveLancarExcecaoSenhaAtualIncorreta() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));

        TrocarSenhaRequest req = new TrocarSenhaRequest();
        req.setSenhaAtual("senhaErrada");
        req.setNovaSenha("novaSenha123");
        req.setConfirmarSenha("novaSenha123");

        assertThatThrownBy(() -> usuarioService.trocarSenha(1L, req))
                .isInstanceOf(SenhaInvalidaException.class)
                .hasMessageContaining("Senha atual incorreta");
    }

    @Test
    @DisplayName("Deve trocar senha com sucesso")
    void deveTrocarSenhaComSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));
        when(usuarioRepository.save(any())).thenReturn(usuarioBase);

        TrocarSenhaRequest req = new TrocarSenhaRequest();
        req.setSenhaAtual("senha123");
        req.setNovaSenha("novaSenha456");
        req.setConfirmarSenha("novaSenha456");

        assertThatCode(() -> usuarioService.trocarSenha(1L, req)).doesNotThrowAnyException();
    }

    // ── validarLogin ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar autenticado=true com credenciais válidas")
    void deveValidarLoginComSucesso() {
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioBase));

        LoginRequest req = new LoginRequest();
        req.setLogin("joao");
        req.setSenha("senha123");

        LoginResponse response = usuarioService.validarLogin(req);
        assertThat(response.isAutenticado()).isTrue();
        assertThat(response.getUsuarioId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve retornar autenticado=false com senha errada")
    void deveRetornarFalsoComSenhaErrada() {
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioBase));

        LoginRequest req = new LoginRequest();
        req.setLogin("joao");
        req.setSenha("senhaErrada");

        LoginResponse response = usuarioService.validarLogin(req);
        assertThat(response.isAutenticado()).isFalse();
    }

    // ── helpers ───────────────────────────────────────────────────────────────────

    private CriarUsuarioRequest buildCriarRequest() {
        EnderecoRequest er = new EnderecoRequest();
        er.setRua("Rua das Flores"); er.setNumero("100");
        er.setCidade("São Paulo"); er.setEstado("SP"); er.setCep("01310-100");

        CriarUsuarioRequest req = new CriarUsuarioRequest();
        req.setNome("João Silva");
        req.setEmail("joao@email.com");
        req.setLogin("joao");
        req.setSenha("senha123");
        req.setTipoUsuario(TipoUsuario.CLIENTE);
        req.setEndereco(er);
        return req;
    }
}
