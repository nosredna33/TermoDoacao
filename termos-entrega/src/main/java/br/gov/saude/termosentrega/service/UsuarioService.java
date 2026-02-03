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
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String hashBanco = usuario.getSenha();
            
            // Converter $2b$ para $2a$ para compatibilidade com jBCrypt
            if (hashBanco.startsWith("$2b$")) {
                hashBanco = "$2a$" + hashBanco.substring(4);
            }
            
            if (usuario.isAtivo() && BCrypt.checkpw(senha, hashBanco)) {
                return Optional.of(usuario);
            }
        }
        
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
        if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2")) {
            usuario.setSenha(BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt()));
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
            
            usuarioRepository.updatePasswordResetToken(email, token, expiracao);
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
                usuarioRepository.updatePassword(usuario.getId(), senhaCriptografada);
                return true;
            }
        }
        
        return false;
    }
}
