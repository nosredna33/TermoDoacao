-- ============================================
-- Usuário administrador inicial
-- Email: admin@saude.gov.br
-- Senha: Admin@123
-- BCrypt hash gerado para a senha
-- ============================================

INSERT OR IGNORE INTO usuario (nome, email, senha, role, ativo) VALUES
('Administrador do Sistema', 'admin@saude.gov.br', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cyhWhRU/JNLWqpxMEjabey9ivI4Lq', 'ADMIN', 1);
