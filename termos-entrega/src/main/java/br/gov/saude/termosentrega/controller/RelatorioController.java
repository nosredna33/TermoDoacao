package br.gov.saude.termosentrega.controller;

import br.gov.saude.termosentrega.model.TermoEntrega;
import br.gov.saude.termosentrega.service.PdfService;
import br.gov.saude.termosentrega.service.TermoEntregaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/relatorios")
public class RelatorioController {
    
    private final TermoEntregaService termoEntregaService;
    private final PdfService pdfService;
    
    public RelatorioController(TermoEntregaService termoEntregaService, PdfService pdfService) {
        this.termoEntregaService = termoEntregaService;
        this.pdfService = pdfService;
    }
    
    @GetMapping("/termo/{uuid}/pdf")
    public ResponseEntity<byte[]> gerarTermoPdf(@PathVariable String uuid) {
        try {
            TermoEntrega termo = termoEntregaService.findByUuid(uuid)
                    .orElseThrow(() -> new RuntimeException("Termo não encontrado"));
            
            byte[] pdfBytes = pdfService.gerarTermoEntregaPdf(termo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "termo_" + uuid + ".pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}
