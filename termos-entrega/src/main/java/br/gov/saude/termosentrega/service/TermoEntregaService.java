package br.gov.saude.termosentrega.service;

import br.gov.saude.termosentrega.dto.TermoEntregaDTO;
import br.gov.saude.termosentrega.model.Municipio;
import br.gov.saude.termosentrega.model.TermoEntrega;
import br.gov.saude.termosentrega.repository.TermoEntregaRepository;
import br.gov.saude.termosentrega.util.StringNormalizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TermoEntregaService {
    
    private final TermoEntregaRepository termoEntregaRepository;
    private final MunicipioService municipioService;
    
    public TermoEntregaService(TermoEntregaRepository termoEntregaRepository, MunicipioService municipioService) {
        this.termoEntregaRepository = termoEntregaRepository;
        this.municipioService = municipioService;
    }
    
    public List<TermoEntrega> findAll(int page, int size) {
        int offset = page * size;
        return termoEntregaRepository.findAll(size, offset);
    }
    
    public long count() {
        return termoEntregaRepository.count();
    }
    
    public Optional<TermoEntrega> findByUuid(String uuid) {
        return termoEntregaRepository.findByUuid(uuid);
    }
    
    public List<TermoEntrega> search(String query, int page, int size) {
        int offset = page * size;
        return termoEntregaRepository.searchFts(query, size, offset);
    }
    
    public long countSearch(String query) {
        return termoEntregaRepository.countSearchFts(query);
    }
    
    public TermoEntrega criarTermoEntrega(TermoEntregaDTO dto) {
        Optional<TermoEntrega> existente = termoEntregaRepository.findByCpf(dto.getCpf());
        if (existente.isPresent()) {
            throw new RuntimeException("Já existe um termo de entrega para este CPF");
        }
        
        TermoEntrega termo = new TermoEntrega();
        termo.setUuid(UUID.randomUUID().toString());
        termo.setTipoEntidade(dto.getTipoEntidade());
        termo.setNomeOrgao(dto.getNomeOrgao());
        termo.setNomeResponsavel(dto.getNomeResponsavel());
        termo.setCpf(dto.getCpf());
        termo.setCargo(dto.getCargo());
        termo.setEmailPessoal(dto.getEmailPessoal());
        termo.setEmailCorporativo(dto.getEmailCorporativo());
        termo.setEndereco(dto.getEndereco());
        termo.setBairro(dto.getBairro());
        termo.setCidade(dto.getCidade());
        termo.setUf(dto.getUf());
        termo.setCep(dto.getCep());
        termo.setNomeMunicipio(dto.getNomeMunicipio());
        termo.setSiglaOrgao(dto.getSiglaOrgao());
        termo.setEnderecoCompletoUnidade(dto.getEnderecoCompletoUnidade());
        termo.setDescricaoEquipamento(dto.getDescricaoEquipamento());
        
        Optional<Municipio> municipioOpt = Optional.empty();
        if (dto.getCodigoIbge() != null && !dto.getCodigoIbge().isEmpty()) {
            municipioOpt = municipioService.findByCodigoIbge(dto.getCodigoIbge());
        }
        
        if (municipioOpt.isEmpty()) {
            List<Municipio> sugestoes = municipioService.buscarMunicipiosSugeridos(dto.getNomeMunicipio(), dto.getUf());
            if (!sugestoes.isEmpty()) {
                Municipio municipio = sugestoes.get(0);
                termo.setCodigoIbge(municipio.getCodigoIbge());
                termo.setNovoMunicipio(false);
            } else {
                Municipio novoMunicipio = municipioService.criarNovoMunicipio(
                        dto.getNomeMunicipio(), 
                        dto.getUf(), 
                        dto.getCodigoIbge()
                );
                termo.setCodigoIbge(novoMunicipio.getCodigoIbge());
                termo.setNovoMunicipio(true);
            }
        } else {
            termo.setCodigoIbge(municipioOpt.get().getCodigoIbge());
            termo.setNovoMunicipio(false);
        }
        
        return termoEntregaRepository.save(termo);
    }
    
    public void deleteById(Long id) {
        termoEntregaRepository.deleteById(id);
    }
}
