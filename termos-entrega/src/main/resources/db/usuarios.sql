-- ============================================
-- Usuário administrador inicial
-- Email: admin@saude.gov.br
-- Senha: admin
-- jBCrypt hash ($2a$) compatível
-- ============================================

INSERT OR IGNORE INTO usuario (nome, email, senha, role, ativo) VALUES
('Administrador do Sistema', 'admin@saude.gov.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1);
