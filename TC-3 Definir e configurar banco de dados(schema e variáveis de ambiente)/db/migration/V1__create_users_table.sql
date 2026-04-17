-- =============================================================================
-- Migration V1: Criação da tabela de usuários
-- Responsável: Luciano Santiago Barbosa
-- Banco: PostgreSQL 16
-- =============================================================================

CREATE TABLE IF NOT EXISTS usuarios (
    id               BIGSERIAL       PRIMARY KEY,
    nome             VARCHAR(150)    NOT NULL,
    email            VARCHAR(150)    NOT NULL UNIQUE,
    login            VARCHAR(100)    NOT NULL UNIQUE,
    senha            VARCHAR(255)    NOT NULL,
    tipo_usuario     VARCHAR(30)     NOT NULL,
    ultima_alteracao TIMESTAMP       NOT NULL DEFAULT NOW(),
    criado_em        TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_usuarios_nome  ON usuarios(nome);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuarios_login ON usuarios(login);
CREATE INDEX IF NOT EXISTS idx_usuarios_tipo  ON usuarios(tipo_usuario);

COMMENT ON TABLE  usuarios                  IS 'Usuários do sistema (DONO_RESTAURANTE e CLIENTE)';
COMMENT ON COLUMN usuarios.tipo_usuario     IS 'Enum: DONO_RESTAURANTE | CLIENTE';
COMMENT ON COLUMN usuarios.senha            IS 'Hash SHA-256 — nunca armazenado como plain text';
COMMENT ON COLUMN usuarios.ultima_alteracao IS 'Atualizado automaticamente via JPA @PreUpdate';
COMMENT ON COLUMN usuarios.criado_em        IS 'Definido uma única vez via JPA @PrePersist';
