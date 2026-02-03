package br.gov.saude.termosentrega.service;

import br.gov.saude.termosentrega.model.Municipio;
import br.gov.saude.termosentrega.repository.MunicipioRepository;
import br.gov.saude.termosentrega.util.LevenshteinDistance;
import br.gov.saude.termosentrega.util.StringNormalizer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MunicipioService {
    
    private final MunicipioRepository municipioRepository;
    
    public MunicipioService(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }
    
    public List<Municipio> findAll() {
        return municipioRepository.findAll();
    }
    
    public Optional<Municipio> findByCodigoIbge(String codigoIbge) {
        return municipioRepository.findByCodigoIbge(codigoIbge);
    }
    
    public List<Municipio> buscarMunicipiosSugeridos(String nome, String uf) {
        String nomeNormalizado = StringNormalizer.normalize(nome);
        
        List<Municipio> municipios = municipioRepository.searchByNome(nomeNormalizado);
        
        if (uf != null && !uf.isEmpty()) {
            municipios = municipios.stream()
                    .filter(m -> m.getUf().equalsIgnoreCase(uf))
                    .collect(Collectors.toList());
        }
        
        Map<Municipio, Integer> distancias = new HashMap<>();
        for (Municipio municipio : municipios) {
            int distancia = LevenshteinDistance.calculate(nomeNormalizado, municipio.getNomeNormalizado());
            distancias.put(municipio, distancia);
        }
        
        return distancias.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    public Municipio criarNovoMunicipio(String nome, String uf, String codigoIbge) {
        String nomeNormalizado = StringNormalizer.normalize(nome);
        
        if (codigoIbge == null || codigoIbge.isEmpty()) {
            codigoIbge = "NOVO_" + UUID.randomUUID().toString().substring(0, 8);
        }
        
        Municipio municipio = new Municipio(nome, nomeNormalizado, uf, codigoIbge);
        return municipioRepository.save(municipio);
    }
    
    public Municipio save(Municipio municipio) {
        return municipioRepository.save(municipio);
    }
}
