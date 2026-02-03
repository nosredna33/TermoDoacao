package br.gov.saude.termosentrega.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TermoEntrega {
    
    private Long id;
    private String uuid;
    private String tipoEntidade;
    private String nomeOrgao;
    private String nomeResponsavel;
    private String cpf;
    private String cargo;
    private String emailPessoal;
    private String emailCorporativo;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String nomeMunicipio;
    private String siglaOrgao;
    private String codigoIbge;
    private String enderecoCompletoUnidade;
    private String descricaoEquipamento;
    private boolean novoMunicipio;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;    

}
