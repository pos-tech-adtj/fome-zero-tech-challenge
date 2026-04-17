-- =============================================================================
-- Migration V3: Dados iniciais para testes
-- Senhas armazenadas como SHA-256:
--   admin123   → 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
--   cliente123 → 09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9
-- =============================================================================

INSERT INTO usuarios (nome, email, login, senha, tipo_usuario, ultima_alteracao, criado_em)
VALUES
    ('Admin Restaurante', 'admin@restaurante.com', 'admin',
     '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
     'DONO_RESTAURANTE', NOW(), NOW()),
    ('Cliente Teste', 'cliente@email.com', 'cliente',
     '09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9',
     'CLIENTE', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

INSERT INTO enderecos (usuario_id, rua, numero, complemento, bairro, cidade, estado, cep)
SELECT u.id, 'Av. Paulista', '1000', 'Loja 1', 'Bela Vista', 'São Paulo', 'SP', '01310-100'
FROM usuarios u WHERE u.login = 'admin'
ON CONFLICT (usuario_id) DO NOTHING;

INSERT INTO enderecos (usuario_id, rua, numero, complemento, bairro, cidade, estado, cep)
SELECT u.id, 'Rua Augusta', '500', 'Apto 42', 'Consolação', 'São Paulo', 'SP', '01305-000'
FROM usuarios u WHERE u.login = 'cliente'
ON CONFLICT (usuario_id) DO NOTHING;
