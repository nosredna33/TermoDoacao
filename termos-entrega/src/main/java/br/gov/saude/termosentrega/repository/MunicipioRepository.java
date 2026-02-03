package br.gov.saude.termosentrega.repository;

import br.gov.saude.termosentrega.model.Municipio;
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
public class MunicipioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public MunicipioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<Municipio> rowMapper = new RowMapper<Municipio>() {
        @Override
        public Municipio mapRow(ResultSet rs, int rowNum) throws SQLException {
            Municipio municipio = new Municipio();
            municipio.setId(rs.getLong("id"));
            municipio.setNome(rs.getString("nome"));
            municipio.setNomeNormalizado(rs.getString("nome_normalizado"));
            municipio.setUf(rs.getString("uf"));
            municipio.setCodigoIbge(rs.getString("codigo_ibge"));
            municipio.setAtivo(rs.getInt("ativo") == 1);
            
            String dataCriacao = rs.getString("data_criacao");
            if (dataCriacao != null) {
                municipio.setDataCriacao(LocalDateTime.parse(dataCriacao.replace(" ", "T")));
            }
            
            return municipio;
        }
    };
    
    public List<Municipio> findAll() {
        String sql = "SELECT * FROM municipio WHERE ativo = 1 ORDER BY nome";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public Optional<Municipio> findByCodigoIbge(String codigoIbge) {
        String sql = "SELECT * FROM municipio WHERE codigo_ibge = ? AND ativo = 1";
        List<Municipio> municipios = jdbcTemplate.query(sql, rowMapper, codigoIbge);
        return municipios.isEmpty() ? Optional.empty() : Optional.of(municipios.get(0));
    }
    
    public List<Municipio> findByNomeNormalizadoAndUf(String nomeNormalizado, String uf) {
        String sql = "SELECT * FROM municipio WHERE nome_normalizado LIKE ? AND uf = ? AND ativo = 1 LIMIT 10";
        return jdbcTemplate.query(sql, rowMapper, "%" + nomeNormalizado + "%", uf);
    }
    
    public List<Municipio> searchByNome(String termo) {
        String sql = "SELECT * FROM municipio WHERE nome_normalizado LIKE ? AND ativo = 1 LIMIT 20";
        return jdbcTemplate.query(sql, rowMapper, "%" + termo + "%");
    }
    
    public Municipio save(Municipio municipio) {
        if (municipio.getId() == null) {
            return insert(municipio);
        } else {
            update(municipio);
            return municipio;
        }
    }
    
    private Municipio insert(Municipio municipio) {
        String sql = "INSERT INTO municipio (nome, nome_normalizado, uf, codigo_ibge, ativo, data_criacao) " +
                     "VALUES (?, ?, ?, ?, ?, datetime('now'))";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, municipio.getNome());
            ps.setString(2, municipio.getNomeNormalizado());
            ps.setString(3, municipio.getUf());
            ps.setString(4, municipio.getCodigoIbge());
            ps.setInt(5, municipio.isAtivo() ? 1 : 0);
            return ps;
        }, keyHolder);
        
        municipio.setId(keyHolder.getKey().longValue());
        return municipio;
    }
    
    private void update(Municipio municipio) {
        String sql = "UPDATE municipio SET nome = ?, nome_normalizado = ?, uf = ?, codigo_ibge = ?, ativo = ? WHERE id = ?";
        jdbcTemplate.update(sql, municipio.getNome(), municipio.getNomeNormalizado(), 
                           municipio.getUf(), municipio.getCodigoIbge(), 
                           municipio.isAtivo() ? 1 : 0, municipio.getId());
    }
}
