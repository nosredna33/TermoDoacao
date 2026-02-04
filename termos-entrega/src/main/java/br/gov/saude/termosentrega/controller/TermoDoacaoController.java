package br.gov.saude.termosentrega.controller;

import br.gov.saude.termosentrega.model.TermoEntrega;
import br.gov.saude.termosentrega.repository.TermoEntregaRepository;
import br.gov.saude.termosentrega.service.PdfService;
import br.gov.saude.termosentrega.service.TermoEntregaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/termos")
public class TermoDoacaoController {
    
    private final TermoEntregaService termoEntregaService;
    private final TermoEntregaRepository termoEntregaRepository;
    private final PdfService pdfService;
    
    private static final String UPLOAD_DIR = "uploads/termos-assinados/";
    
    public TermoDoacaoController(TermoEntregaService termoEntregaService, 
                                 TermoEntregaRepository termoEntregaRepository,
                                 PdfService pdfService) {
        this.termoEntregaService = termoEntregaService;
        this.termoEntregaRepository = termoEntregaRepository;
        this.pdfService = pdfService;
        
        // Criar diretório de uploads se não existir
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @GetMapping("/{uuid}/termo-doacao/download")
    public ResponseEntity<byte[]> downloadTermoDoacao(@PathVariable String uuid, HttpSession session) {
        // Verificar autenticação
        if (session.getAttribute("usuarioLogado") == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Optional<TermoEntrega> termoOpt = termoEntregaService.findByUuid(uuid);
            if (!termoOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            TermoEntrega termo = termoOpt.get();
            if (termo == null) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] pdfBytes = pdfService.gerarTermoDoacaoPdf(termo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "Termo_Doacao_" + termo.getNomeMunicipio().replaceAll(" ", "_") + "_" + uuid.substring(0, 8) + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{uuid}/termo-doacao/visualizar")
    public ResponseEntity<byte[]> visualizarTermoDoacao(@PathVariable String uuid, HttpSession session) {
        // Verificar autenticação
        if (session.getAttribute("usuarioLogado") == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Optional<TermoEntrega> termoOpt = termoEntregaService.findByUuid(uuid);
            if (!termoOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            TermoEntrega termo = termoOpt.get();
            if (termo == null) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] pdfBytes = pdfService.gerarTermoDoacaoPdf(termo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{uuid}/termo-doacao/upload")
    public String uploadTermoAssinado(
            @PathVariable String uuid,
            @RequestParam("arquivo") MultipartFile arquivo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Verificar autenticação
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/auth/login";
        }
        
        try {
            Optional<TermoEntrega> termoOpt = termoEntregaService.findByUuid(uuid);
            if (!termoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("erro", "Termo não encontrado");
                return "redirect:/admin/termos";
            }
            TermoEntrega termo = termoOpt.get();
            
            // Validar arquivo
            if (arquivo.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Arquivo não pode ser vazio");
                return "redirect:/admin/termos/" + uuid;
            }
            
            // Validar tipo de arquivo (PDF)
            String contentType = arquivo.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                redirectAttributes.addFlashAttribute("erro", "Apenas arquivos PDF são permitidos");
                return "redirect:/admin/termos/" + uuid;
            }
            
            // Validar tamanho (máximo 10MB)
            if (arquivo.getSize() > 10 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("erro", "Arquivo muito grande. Máximo 10MB");
                return "redirect:/admin/termos/" + uuid;
            }
            
            // Gerar nome único para o arquivo
            String nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            Path caminhoArquivo = Paths.get(UPLOAD_DIR + nomeArquivo);
            
            // Salvar arquivo
            Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
            
            // Atualizar termo no banco
            termo.setArquivoTermoAssinado(nomeArquivo);
            termo.setDataAssinatura(LocalDateTime.now());
            termo.setStatusAssinatura("ASSINADO");
            termoEntregaRepository.save(termo);
            
            redirectAttributes.addFlashAttribute("sucesso", "Termo assinado enviado com sucesso!");
            return "redirect:/admin/termos/" + uuid;
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao fazer upload do arquivo: " + e.getMessage());
            return "redirect:/admin/termos/" + uuid;
        }
    }
    
    @GetMapping("/{uuid}/termo-assinado/download")
    public ResponseEntity<byte[]> downloadTermoAssinado(@PathVariable String uuid, HttpSession session) {
        // Verificar autenticação
        if (session.getAttribute("usuarioLogado") == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Optional<TermoEntrega> termoOpt = termoEntregaService.findByUuid(uuid);
            if (!termoOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            TermoEntrega termo = termoOpt.get();
            if (termo == null || termo.getArquivoTermoAssinado() == null) {
                return ResponseEntity.notFound().build();
            }
            
            Path caminhoArquivo = Paths.get(UPLOAD_DIR + termo.getArquivoTermoAssinado());
            if (!Files.exists(caminhoArquivo)) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] arquivoBytes = Files.readAllBytes(caminhoArquivo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", termo.getArquivoTermoAssinado());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(arquivoBytes);
                    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
