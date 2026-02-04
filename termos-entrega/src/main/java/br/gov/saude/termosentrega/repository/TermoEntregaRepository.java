package br.gov.saude.termosentrega.repository;

import br.gov.saude.termosentrega.model.TermoEntrega;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TermoEntregaRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public TermoEntregaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<TermoEntrega> rowMapper = new RowMapper<TermoEntrega>() {
        @Override
        public TermoEntrega mapRow(ResultSet rs, int rowNum) throws SQLException {
            TermoEntrega termo = new TermoEntrega();
            termo.setId(rs.getLong("id"));
            termo.setUuid(rs.getString("uuid"));
            termo.setTipoEntidade(rs.getString("tipo_entidade"));
            termo.setNomeOrgao(rs.getString("nome_orgao"));
            termo.setNomeResponsavel(rs.getString("nome_responsavel"));
            termo.setCpf(rs.getString("cpf"));
            termo.setCargo(rs.getString("cargo"));
            termo.setEmailPessoal(rs.getString("email_pessoal"));
            termo.setEmailCorporativo(rs.getString("email_corporativo"));
            termo.setEndereco(rs.getString("endereco"));
            termo.setBairro(rs.getString("bairro"));
            termo.setCidade(rs.getString("cidade"));
            termo.setUf(rs.getString("uf"));
            termo.setCep(rs.getString("cep"));
            termo.setNomeMunicipio(rs.getString("nome_municipio"));
            termo.setSiglaOrgao(rs.getString("sigla_orgao"));
            termo.setCodigoIbge(rs.getString("codigo_ibge"));
            termo.setEnderecoCompletoUnidade(rs.getString("endereco_completo_unidade"));
            termo.setDescricaoEquipamento(rs.getString("descricao_equipamento"));
            termo.setNovoMunicipio(rs.getInt("novo_municipio") == 1);
            
            String dataCriacao = rs.getString("data_criacao");
            if (dataCriacao != null) {
                termo.setDataCriacao(LocalDateTime.parse(dataCriacao.replace(" ", "T")));
            }
            
            String dataAtualizacao = rs.getString("data_atualizacao");
            if (dataAtualizacao != null) {
                termo.setDataAtualizacao(LocalDateTime.parse(dataAtualizacao.replace(" ", "T")));
            }
            
            termo.setArquivoTermoAssinado(rs.getString("arquivo_termo_assinado"));
            
            String dataAssinatura = rs.getString("data_assinatura");
            if (dataAssinatura != null && !dataAssinatura.isEmpty()) {
                termo.setDataAssinatura(LocalDateTime.parse(dataAssinatura.replace(" ", "T")));
            }
            
            String statusAssinatura = rs.getString("status_assinatura");
            termo.setStatusAssinatura(statusAssinatura != null ? statusAssinatura : "PENDENTE");
            
            return termo;
        }
    };
    
    public List<TermoEntrega> findAll(int limit, int offset) {
        String sql = "SELECT * FROM termo_entrega ORDER BY data_criacao DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, limit, offset);
    }
    
    public long count() {
        String sql = "SELECT COUNT(*) FROM termo_entrega";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    public Optional<TermoEntrega> findByUuid(String uuid) {
        String sql = "SELECT * FROM termo_entrega WHERE uuid = ?";
        List<TermoEntrega> termos = jdbcTemplate.query(sql, rowMapper, uuid);
        return termos.isEmpty() ? Optional.empty() : Optional.of(termos.get(0));
    }
    
    public Optional<TermoEntrega> findByCpf(String cpf) {
        String sql = "SELECT * FROM termo_entrega WHERE cpf = ?";
        List<TermoEntrega> termos = jdbcTemplate.query(sql, rowMapper, cpf);
        return termos.isEmpty() ? Optional.empty() : Optional.of(termos.get(0));
    }
    
    public List<TermoEntrega> searchFts(String query, int limit, int offset) {
        String searchPattern = "%" + query + "%";
        String sql = "SELECT * FROM termo_entrega WHERE " +
                     "nome_responsavel LIKE ? OR " +
                     "cpf LIKE ? OR " +
                     "email_pessoal LIKE ? OR " +
                     "nome_municipio LIKE ? OR " +
                     "nome_orgao LIKE ? " +
                     "ORDER BY data_criacao DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, searchPattern, searchPattern, searchPattern, searchPattern, searchPattern, limit, offset);
    }
    
    public long countSearchFts(String query) {
        String searchPattern = "%" + query + "%";
        String sql = "SELECT COUNT(*) FROM termo_entrega WHERE " +
                     "nome_responsavel LIKE ? OR " +
                     "cpf LIKE ? OR " +
                     "email_pessoal LIKE ? OR " +
                     "nome_municipio LIKE ? OR " +
                     "nome_orgao LIKE ?";
        return jdbcTemplate.queryForObject(sql, Long.class, searchPattern, searchPattern, searchPattern, searchPattern, searchPattern);
    }
    
    public TermoEntrega save(TermoEntrega termo) {
        if (termo.getId() == null) {
            return insert(termo);
        } else {
            update(termo);
            return termo;
        }
    }
    
    private TermoEntrega insert(TermoEntrega termo) {
        String sql = "INSERT INTO termo_entrega (uuid, tipo_entidade, nome_orgao, nome_responsavel, cpf, cargo, " +
                     "email_pessoal, email_corporativo, endereco, bairro, cidade, uf, cep, nome_municipio, " +
                     "sigla_orgao, codigo_ibge, endereco_completo_unidade, descricao_equipamento, novo_municipio, " +
                     "data_criacao, data_atualizacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now'), datetime('now'))";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, termo.getUuid());
            ps.setString(2, termo.getTipoEntidade());
            ps.setString(3, termo.getNomeOrgao());
            ps.setString(4, termo.getNomeResponsavel());
            ps.setString(5, termo.getCpf());
            ps.setString(6, termo.getCargo());
            ps.setString(7, termo.getEmailPessoal());
            ps.setString(8, termo.getEmailCorporativo());
            ps.setString(9, termo.getEndereco());
            ps.setString(10, termo.getBairro());
            ps.setString(11, termo.getCidade());
            ps.setString(12, termo.getUf());
            ps.setString(13, termo.getCep());
            ps.setString(14, termo.getNomeMunicipio());
            ps.setString(15, termo.getSiglaOrgao());
            ps.setString(16, termo.getCodigoIbge());
            ps.setString(17, termo.getEnderecoCompletoUnidade());
            ps.setString(18, termo.getDescricaoEquipamento());
            ps.setInt(19, termo.isNovoMunicipio() ? 1 : 0);
            return ps;
        }, keyHolder);
        
        termo.setId(keyHolder.getKey().longValue());
        return termo;
    }
    
    private void update(TermoEntrega termo) {
        String sql = "UPDATE termo_entrega SET tipo_entidade = ?, nome_orgao = ?, nome_responsavel = ?, cpf = ?, " +
                     "cargo = ?, email_pessoal = ?, email_corporativo = ?, endereco = ?, bairro = ?, cidade = ?, " +
                     "uf = ?, cep = ?, nome_municipio = ?, sigla_orgao = ?, codigo_ibge = ?, " +
                     "endereco_completo_unidade = ?, descricao_equipamento = ?, novo_municipio = ?, " +
                     "arquivo_termo_assinado = ?, data_assinatura = ?, status_assinatura = ?, " +
                     "data_atualizacao = datetime('now') WHERE id = ?";
        
        jdbcTemplate.update(sql, termo.getTipoEntidade(), termo.getNomeOrgao(), termo.getNomeResponsavel(),
                           termo.getCpf(), termo.getCargo(), termo.getEmailPessoal(), termo.getEmailCorporativo(),
                           termo.getEndereco(), termo.getBairro(), termo.getCidade(), termo.getUf(), termo.getCep(),
                           termo.getNomeMunicipio(), termo.getSiglaOrgao(), termo.getCodigoIbge(),
                           termo.getEnderecoCompletoUnidade(), termo.getDescricaoEquipamento(),
                           termo.isNovoMunicipio() ? 1 : 0,
                           termo.getArquivoTermoAssinado(), termo.getDataAssinatura(), termo.getStatusAssinatura(),
                           termo.getId());
    }
    
    public void deleteById(Long id) {
        String sql = "DELETE FROM termo_entrega WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public List<java.util.Map<String, Object>> countByEstado() {
        String sql = "SELECT uf, COUNT(*) as total FROM termo_entrega GROUP BY uf ORDER BY total DESC";
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<java.util.Map<String, Object>> countByRegiao() {
        String sql = "SELECT " +
                     "CASE " +
                     "  WHEN uf IN ('AC', 'AM', 'AP', 'PA', 'RO', 'RR', 'TO') THEN 'Norte' " +
                     "  WHEN uf IN ('AL', 'BA', 'CE', 'MA', 'PB', 'PE', 'PI', 'RN', 'SE') THEN 'Nordeste' " +
                     "  WHEN uf IN ('DF', 'GO', 'MT', 'MS') THEN 'Centro-Oeste' " +
                     "  WHEN uf IN ('ES', 'MG', 'RJ', 'SP') THEN 'Sudeste' " +
                     "  WHEN uf IN ('PR', 'RS', 'SC') THEN 'Sul' " +
                     "  ELSE 'Não Informado' " +
                     "END as regiao, COUNT(*) as total " +
                     "FROM termo_entrega " +
                     "GROUP BY regiao " +
                     "ORDER BY total DESC";
        return jdbcTemplate.queryForList(sql);
    }
}
