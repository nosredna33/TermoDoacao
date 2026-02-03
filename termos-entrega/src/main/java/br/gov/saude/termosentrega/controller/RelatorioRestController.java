package br.gov.saude.termosentrega.controller;

import br.gov.saude.termosentrega.repository.TermoEntregaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioRestController {
    
    private final TermoEntregaRepository termoEntregaRepository;
    
    public RelatorioRestController(TermoEntregaRepository termoEntregaRepository) {
        this.termoEntregaRepository = termoEntregaRepository;
    }
    
    @GetMapping("/por-estado")
    public List<Map<String, Object>> relatorioPorEstado() {
        return termoEntregaRepository.countByEstado();
    }
    
    @GetMapping("/por-regiao")
    public List<Map<String, Object>> relatorioPorRegiao() {
        return termoEntregaRepository.countByRegiao();
    }
}
