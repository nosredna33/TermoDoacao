package br.gov.saude.termosentrega.controller;

import br.gov.saude.termosentrega.model.TermoEntrega;
import br.gov.saude.termosentrega.model.Usuario;
import br.gov.saude.termosentrega.service.TermoEntregaService;
import br.gov.saude.termosentrega.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final TermoEntregaService termoEntregaService;
    private final UsuarioService usuarioService;
    
    public AdminController(TermoEntregaService termoEntregaService, UsuarioService usuarioService) {
        this.termoEntregaService = termoEntregaService;
        this.usuarioService = usuarioService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalTermos = termoEntregaService.count();
        model.addAttribute("totalTermos", totalTermos);
        return "admin/dashboard";
    }
    
    @GetMapping("/termos")
    public String listarTermos(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model) {
        List<TermoEntrega> termos = termoEntregaService.findAll(page, size);
        long total = termoEntregaService.count();
        int totalPages = (int) Math.ceil((double) total / size);
        
        model.addAttribute("termos", termos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        
        return "admin/termos-lista";
    }
    
    @GetMapping("/termos/buscar")
    public String buscarTermos(@RequestParam String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model) {
        List<TermoEntrega> termos = termoEntregaService.search(query, page, size);
        long total = termoEntregaService.countSearch(query);
        int totalPages = (int) Math.ceil((double) total / size);
        
        model.addAttribute("termos", termos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("query", query);
        
        return "admin/termos-lista";
    }
    
    @GetMapping("/termos/{uuid}")
    public String verTermo(@PathVariable String uuid, Model model) {
        TermoEntrega termo = termoEntregaService.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Termo não encontrado"));
        model.addAttribute("termo", termo);
        return "admin/termo-detalhes";
    }
    
    @PostMapping("/termos/{id}/deletar")
    public String deletarTermo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        termoEntregaService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Termo deletado com sucesso");
        return "redirect:/admin/termos";
    }
    
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios-lista";
    }
    
    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuario-form";
    }
    
    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            // Verificar se já existe usuário com este e-mail (exceto se for edição)
            if (usuario.getId() == null) {
                if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Já existe um usuário cadastrado com este e-mail");
                    return "redirect:/admin/usuarios/novo";
                }
            }
            
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("message", "Usuário salvo com sucesso");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar usuário: " + e.getMessage());
            return "redirect:/admin/usuarios/novo";
        }
    }
    
    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "admin/usuario-form";
    }
    
    @PostMapping("/usuarios/{id}/deletar")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Usuário desativado com sucesso");
        return "redirect:/admin/usuarios";
    }
}
