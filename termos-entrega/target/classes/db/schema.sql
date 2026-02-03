-- ============================================
-- Schema para Sistema de Termos de Entrega
-- SQLite3
-- ============================================

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    senha TEXT NOT NULL,
    role TEXT NOT NULL CHECK(role IN ('ADMIN', 'USER')),
    ativo INTEGER NOT NULL DEFAULT 1,
    token_recuperacao_senha TEXT,
    token_expiracao DATETIME,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_usuario_token ON usuario(token_recuperacao_senha);

-- Tabela de Municípios
CREATE TABLE IF NOT EXISTS municipio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    nome_normalizado TEXT NOT NULL,
    uf TEXT NOT NULL,
    codigo_ibge TEXT NOT NULL UNIQUE,
    ativo INTEGER NOT NULL DEFAULT 1,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_municipio_nome_normalizado ON municipio(nome_normalizado);
CREATE INDEX IF NOT EXISTS idx_municipio_uf ON municipio(uf);
CREATE INDEX IF NOT EXISTS idx_municipio_codigo_ibge ON municipio(codigo_ibge);

-- Tabela de Termos de Entrega
CREATE TABLE IF NOT EXISTS termo_entrega (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT NOT NULL UNIQUE,
    tipo_entidade TEXT NOT NULL CHECK(tipo_entidade IN ('PREFEITO', 'SECRETARIO_MUNICIPAL_SAUDE')),
    nome_orgao TEXT NOT NULL,
    nome_responsavel TEXT NOT NULL,
    cpf TEXT NOT NULL UNIQUE,
    cargo TEXT NOT NULL,
    email_pessoal TEXT NOT NULL,
    email_corporativo TEXT,
    endereco TEXT NOT NULL,
    bairro TEXT NOT NULL,
    cidade TEXT NOT NULL,
    uf TEXT NOT NULL,
    cep TEXT NOT NULL,
    nome_municipio TEXT NOT NULL,
    sigla_orgao TEXT,
    codigo_ibge TEXT,
    endereco_completo_unidade TEXT NOT NULL,
    descricao_equipamento TEXT NOT NULL,
    novo_municipio INTEGER NOT NULL DEFAULT 0,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_termo_uuid ON termo_entrega(uuid);
CREATE INDEX IF NOT EXISTS idx_termo_cpf ON termo_entrega(cpf);
CREATE INDEX IF NOT EXISTS idx_termo_codigo_ibge ON termo_entrega(codigo_ibge);
CREATE INDEX IF NOT EXISTS idx_termo_data_criacao ON termo_entrega(data_criacao);
CREATE INDEX IF NOT EXISTS idx_termo_nome_responsavel ON termo_entrega(nome_responsavel);
CREATE INDEX IF NOT EXISTS idx_termo_nome_municipio ON termo_entrega(nome_municipio);
