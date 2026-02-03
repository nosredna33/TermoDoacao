package br.gov.saude.termosentrega.controller;

import br.gov.saude.termosentrega.model.Usuario;
import br.gov.saude.termosentrega.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private final UsuarioService usuarioService;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                           @RequestParam(required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "E-mail ou senha inválidos");
        }
        if (logout != null) {
            model.addAttribute("message", "Logout realizado com sucesso");
        }
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(username, password);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogado", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNome", usuario.getNome());
            session.setAttribute("usuarioRole", usuario.getRole());
            return "redirect:/admin/dashboard";
        }
        
        return "redirect:/auth/login?error=true";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login?logout=true";
    }
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                 RedirectAttributes redirectAttributes) {
        try {
            usuarioService.solicitarRecuperacaoSenha(email, baseUrl);
            redirectAttributes.addFlashAttribute("message", 
                "Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erro ao processar solicitação. Tente novamente mais tarde.");
        }
        
        return "redirect:/auth/forgot-password";
    }
    
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        if (!usuarioService.validarTokenRecuperacao(token)) {
            model.addAttribute("error", "Token inválido ou expirado");
            return "auth/reset-password";
        }
        
        model.addAttribute("token", token);
        return "auth/reset-password";
    }
    
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               RedirectAttributes redirectAttributes) {
        
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "As senhas não coincidem");
            return "redirect:/auth/reset-password?token=" + token;
        }
        
        if (usuarioService.redefinirSenha(token, password)) {
            redirectAttributes.addFlashAttribute("message", 
                "Senha redefinida com sucesso! Faça login com sua nova senha.");
            return "redirect:/auth/login";
        }
        
        redirectAttributes.addFlashAttribute("error", "Token inválido ou expirado");
        return "redirect:/auth/reset-password?token=" + token;
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
