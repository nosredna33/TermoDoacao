-- ============================================
-- Triggers FTS5 para Sistema de Termos de Entrega
-- ============================================

-- Trigger para INSERT
CREATE TRIGGER IF NOT EXISTS termo_entrega_ai AFTER INSERT ON termo_entrega
BEGIN
    INSERT INTO termo_entrega_fts(rowid, uuid, tipo_entidade, nome_orgao, nome_responsavel, cpf, cargo, email_pessoal, email_corporativo, endereco, bairro, cidade, uf, cep, nome_municipio, sigla_orgao, codigo_ibge, endereco_completo_unidade, descricao_equipamento)
    VALUES (new.id, new.uuid, new.tipo_entidade, new.nome_orgao, new.nome_responsavel, new.cpf, new.cargo, new.email_pessoal, new.email_corporativo, new.endereco, new.bairro, new.cidade, new.uf, new.cep, new.nome_municipio, new.sigla_orgao, new.codigo_ibge, new.endereco_completo_unidade, new.descricao_equipamento);
END;

-- Trigger para DELETE
CREATE TRIGGER IF NOT EXISTS termo_entrega_ad AFTER DELETE ON termo_entrega
BEGIN
    INSERT INTO termo_entrega_fts(termo_entrega_fts, rowid, uuid, tipo_entidade, nome_orgao, nome_responsavel, cpf, cargo, email_pessoal, email_corporativo, endereco, bairro, cidade, uf, cep, nome_municipio, sigla_orgao, codigo_ibge, endereco_completo_unidade, descricao_equipamento)
    VALUES('delete', old.id, old.uuid, old.tipo_entidade, old.nome_orgao, old.nome_responsavel, old.cpf, old.cargo, old.email_pessoal, old.email_corporativo, old.endereco, old.bairro, old.cidade, old.uf, old.cep, old.nome_municipio, old.sigla_orgao, old.codigo_ibge, old.endereco_completo_unidade, old.descricao_equipamento);
END;

-- Trigger para UPDATE
CREATE TRIGGER IF NOT EXISTS termo_entrega_au AFTER UPDATE ON termo_entrega
BEGIN
    INSERT INTO termo_entrega_fts(termo_entrega_fts, rowid, uuid, tipo_entidade, nome_orgao, nome_responsavel, cpf, cargo, email_pessoal, email_corporativo, endereco, bairro, cidade, uf, cep, nome_municipio, sigla_orgao, codigo_ibge, endereco_completo_unidade, descricao_equipamento)
    VALUES('delete', old.id, old.uuid, old.tipo_entidade, old.nome_orgao, old.nome_responsavel, old.cpf, old.cargo, old.email_pessoal, old.email_corporativo, old.endereco, old.bairro, old.cidade, old.uf, old.cep, old.nome_municipio, old.sigla_orgao, old.codigo_ibge, old.endereco_completo_unidade, old.descricao_equipamento);
    INSERT INTO termo_entrega_fts(rowid, uuid, tipo_entidade, nome_orgao, nome_responsavel, cpf, cargo, email_pessoal, email_corporativo, endereco, bairro, cidade, uf, cep, nome_municipio, sigla_orgao, codigo_ibge, endereco_completo_unidade, descricao_equipamento)
    VALUES (new.id, new.uuid, new.tipo_entidade, new.nome_orgao, new.nome_responsavel, new.cpf, new.cargo, new.email_pessoal, new.email_corporativo, new.endereco, new.bairro, new.cidade, new.uf, new.cep, new.nome_municipio, new.sigla_orgao, new.codigo_ibge, new.endereco_completo_unidade, new.descricao_equipamento);
END;
