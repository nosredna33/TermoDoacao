package br.gov.saude.termosentrega.service;

import br.gov.saude.termosentrega.model.Usuario;
import br.gov.saude.termosentrega.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    
    public UsuarioService(UsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }
    
    public Optional<Usuario> autenticar(String email, String senha) {
        System.out.println("[AUTH] Tentando autenticar: " + email);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("[AUTH] Usuário encontrado: " + usuario.getNome());
            System.out.println("[AUTH] Usuário ativo: " + usuario.isAtivo());
            System.out.println("[AUTH] Hash no banco: " + usuario.getSenha().substring(0, 20) + "...");
            System.out.println("[AUTH] Senha fornecida: " + senha);
            
            boolean senhaCorreta = BCrypt.checkpw(senha, usuario.getSenha());
            System.out.println("[AUTH] Senha correta: " + senhaCorreta);
            
            if (usuario.isAtivo() && senhaCorreta) {
                System.out.println("[AUTH] Autenticação bem-sucedida!");
                return Optional.of(usuario);
            }
        } else {
            System.out.println("[AUTH] Usuário não encontrado");
        }
        
        System.out.println("[AUTH] Autenticação falhou");
        return Optional.empty();
    }
    
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            // Novo usuário - criptografar senha
            String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
            usuario.setSenha(senhaCriptografada);
        } else {
            // Atualização - verificar se senha foi alterada
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
            if (usuarioExistente.isPresent()) {
                String senhaAtual = usuarioExistente.get().getSenha();
                if (!usuario.getSenha().equals(senhaAtual)) {
                    // Senha foi alterada, criptografar
                    String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
                    usuario.setSenha(senhaCriptografada);
                }
            }
        }
        
        return usuarioRepository.save(usuario);
    }
    
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    public void solicitarRecuperacaoSenha(String email, String baseUrl) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = UUID.randomUUID().toString();
            LocalDateTime expiracao = LocalDateTime.now().plusHours(24);
            
            usuario.setTokenRecuperacaoSenha(token);
            usuario.setTokenExpiracao(expiracao);
            usuarioRepository.save(usuario);
            
            String resetUrl = baseUrl + "/auth/reset-password?token=" + token;
            emailService.enviarEmailRecuperacaoSenha(usuario.getEmail(), resetUrl);
        }
    }
    
    public boolean validarTokenRecuperacao(String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByPasswordResetToken(token);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return usuario.getTokenExpiracao() != null && 
                   usuario.getTokenExpiracao().isAfter(LocalDateTime.now());
        }
        
        return false;
    }
    
    public boolean redefinirSenha(String token, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByPasswordResetToken(token);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            if (usuario.getTokenExpiracao() != null && 
                usuario.getTokenExpiracao().isAfter(LocalDateTime.now())) {
                
                String senhaCriptografada = BCrypt.hashpw(novaSenha, BCrypt.gensalt());
                usuario.setSenha(senhaCriptografada);
                usuario.setTokenRecuperacaoSenha(null);
                usuario.setTokenExpiracao(null);
                usuarioRepository.save(usuario);
                
                return true;
            }
        }
        
        return false;
    }
}
