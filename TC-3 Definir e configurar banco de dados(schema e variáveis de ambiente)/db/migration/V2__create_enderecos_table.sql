-- =============================================================================
-- Migration V2: Criação da tabela de endereços (separada de usuários)
-- Responsável: Luciano Santiago Barbosa
-- Banco: PostgreSQL 16
-- =============================================================================

CREATE TABLE IF NOT EXISTS enderecos (
    id           BIGSERIAL       PRIMARY KEY,
    usuario_id   BIGINT          NOT NULL UNIQUE,
    rua          VARCHAR(200)    NOT NULL,
    numero       VARCHAR(20)     NOT NULL,
    complemento  VARCHAR(100),
    bairro       VARCHAR(100),
    cidade       VARCHAR(100)    NOT NULL,
    estado       VARCHAR(2)      NOT NULL,
    cep          VARCHAR(9)      NOT NULL,

    CONSTRAINT fk_endereco_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_enderecos_usuario_id ON enderecos(usuario_id);

COMMENT ON TABLE  enderecos            IS 'Endereços dos usuários — relação 1:1 com usuarios';
COMMENT ON COLUMN enderecos.usuario_id IS 'FK para usuarios(id) — CASCADE DELETE';
COMMENT ON COLUMN enderecos.estado     IS 'UF com 2 caracteres, ex: SP, RJ, MG';
COMMENT ON COLUMN enderecos.cep        IS 'Formato 00000-000, validado na camada da API';
