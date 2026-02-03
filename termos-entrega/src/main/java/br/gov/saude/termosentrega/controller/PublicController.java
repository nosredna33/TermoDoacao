package br.gov.saude.termosentrega.controller;

import br.gov.saude.termosentrega.dto.TermoEntregaDTO;
import br.gov.saude.termosentrega.model.Municipio;
import br.gov.saude.termosentrega.model.TermoEntrega;
import br.gov.saude.termosentrega.service.MunicipioService;
import br.gov.saude.termosentrega.service.TermoEntregaService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class PublicController {
    
    private final TermoEntregaService termoEntregaService;
    private final MunicipioService municipioService;
    
    public PublicController(TermoEntregaService termoEntregaService, MunicipioService municipioService) {
        this.termoEntregaService = termoEntregaService;
        this.municipioService = municipioService;
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/public/formulario";
    }
    
    @GetMapping("/public/formulario")
    public String formulario(Model model) {
        model.addAttribute("termoEntregaDTO", new TermoEntregaDTO());
        return "public/formulario";
    }
    
    @PostMapping("/public/formulario")
    public String submitFormulario(@Valid @ModelAttribute TermoEntregaDTO termoEntregaDTO,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "public/formulario";
        }
        
        try {
            TermoEntrega termo = termoEntregaService.criarTermoEntrega(termoEntregaDTO);
            redirectAttributes.addFlashAttribute("message", 
                "Termo de entrega registrado com sucesso! UUID: " + termo.getUuid());
            return "redirect:/public/sucesso?uuid=" + termo.getUuid();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "public/formulario";
        }
    }
    
    @GetMapping("/public/sucesso")
    public String sucesso(@RequestParam String uuid, Model model) {
        model.addAttribute("uuid", uuid);
        return "public/sucesso";
    }
    
    @GetMapping("/api/municipios/buscar")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> buscarMunicipios(@RequestParam String termo,
                                                                       @RequestParam(required = false) String uf) {
        List<Municipio> municipios = municipioService.buscarMunicipiosSugeridos(termo, uf);
        
        List<Map<String, String>> resultado = municipios.stream().map(m -> {
            Map<String, String> map = new HashMap<>();
            map.put("nome", m.getNome());
            map.put("uf", m.getUf());
            map.put("codigoIbge", m.getCodigoIbge());
            return map;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(resultado);
    }
}
