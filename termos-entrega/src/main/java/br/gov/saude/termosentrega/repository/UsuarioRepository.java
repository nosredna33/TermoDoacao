package br.gov.saude.termosentrega.repository;

import br.gov.saude.termosentrega.model.Usuario;
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
public class UsuarioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<Usuario> rowMapper = new RowMapper<Usuario>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setNome(rs.getString("nome"));
            usuario.setEmail(rs.getString("email"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setRole(rs.getString("role"));
            usuario.setAtivo(rs.getInt("ativo") == 1);
            usuario.setTokenRecuperacaoSenha(rs.getString("token_recuperacao_senha"));
            
            String tokenExpiracao = rs.getString("token_expiracao");
            if (tokenExpiracao != null) {
                usuario.setTokenExpiracao(LocalDateTime.parse(tokenExpiracao.replace(" ", "T")));
            }
            
            String dataCriacao = rs.getString("data_criacao");
            if (dataCriacao != null) {
                usuario.setDataCriacao(LocalDateTime.parse(dataCriacao.replace(" ", "T")));
            }
            
            String dataAtualizacao = rs.getString("data_atualizacao");
            if (dataAtualizacao != null) {
                usuario.setDataAtualizacao(LocalDateTime.parse(dataAtualizacao.replace(" ", "T")));
            }
            
            return usuario;
        }
    };
    
    public Optional<Usuario> findByEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND ativo = 1";
        List<Usuario> usuarios = jdbcTemplate.query(sql, rowMapper, email);
        return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
    }
    
    public Optional<Usuario> findById(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        List<Usuario> usuarios = jdbcTemplate.query(sql, rowMapper, id);
        return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
    }
    
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuario ORDER BY nome";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            return insert(usuario);
        } else {
            update(usuario);
            return usuario;
        }
    }
    
    private Usuario insert(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, role, ativo, data_criacao, data_atualizacao) " +
                     "VALUES (?, ?, ?, ?, ?, datetime('now'), datetime('now'))";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getRole());
            ps.setInt(5, usuario.isAtivo() ? 1 : 0);
            return ps;
        }, keyHolder);
        
        usuario.setId(keyHolder.getKey().longValue());
        return usuario;
    }
    
    private void update(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, role = ?, ativo = ?, " +
                     "data_atualizacao = datetime('now') WHERE id = ?";
        
        jdbcTemplate.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
                           usuario.getRole(), usuario.isAtivo() ? 1 : 0, usuario.getId());
    }
    
    public void updatePasswordResetToken(String email, String token, LocalDateTime expiracao) {
        String sql = "UPDATE usuario SET token_recuperacao_senha = ?, token_expiracao = ?, " +
                     "data_atualizacao = datetime('now') WHERE email = ?";
        
        jdbcTemplate.update(sql, token, expiracao.toString().replace("T", " "), email);
    }
    
    public Optional<Usuario> findByPasswordResetToken(String token) {
        String sql = "SELECT * FROM usuario WHERE token_recuperacao_senha = ? AND ativo = 1";
        List<Usuario> usuarios = jdbcTemplate.query(sql, rowMapper, token);
        return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
    }
    
    public void updatePassword(Long id, String novaSenha) {
        String sql = "UPDATE usuario SET senha = ?, token_recuperacao_senha = NULL, " +
                     "token_expiracao = NULL, data_atualizacao = datetime('now') WHERE id = ?";
        
        jdbcTemplate.update(sql, novaSenha, id);
    }
    
    public void deleteById(Long id) {
        String sql = "UPDATE usuario SET ativo = 0, data_atualizacao = datetime('now') WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
