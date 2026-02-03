package br.gov.saude.termosentrega.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TermoEntregaDTO {
    
    @NotBlank(message = "Tipo de entidade é obrigatório")
    private String tipoEntidade;
    
    @NotBlank(message = "Nome do órgão é obrigatório")
    private String nomeOrgao;
    
    @NotBlank(message = "Nome do responsável é obrigatório")
    private String nomeResponsavel;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF inválido")
    private String cpf;
    
    @NotBlank(message = "Cargo é obrigatório")
    private String cargo;
    
    @NotBlank(message = "E-mail pessoal é obrigatório")
    @Email(message = "E-mail pessoal inválido")
    private String emailPessoal;
    
    @Email(message = "E-mail corporativo inválido")
    private String emailCorporativo;
    
    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;
    
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;
    
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    
    @NotBlank(message = "UF é obrigatória")
    private String uf;
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido")
    private String cep;
    
    @NotBlank(message = "Nome do município é obrigatório")
    private String nomeMunicipio;
    
    private String siglaOrgao;
    
    private String codigoIbge;
    
    @NotBlank(message = "Endereço completo da unidade é obrigatório")
    private String enderecoCompletoUnidade;
    
    @NotBlank(message = "Descrição do equipamento é obrigatória")
    private String descricaoEquipamento;
    
    public String getTipoEntidade() {
        return tipoEntidade;
    }
    
    public void setTipoEntidade(String tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }
    
    public String getNomeOrgao() {
        return nomeOrgao;
    }
    
    public void setNomeOrgao(String nomeOrgao) {
        this.nomeOrgao = nomeOrgao;
    }
    
    public String getNomeResponsavel() {
        return nomeResponsavel;
    }
    
    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public String getEmailPessoal() {
        return emailPessoal;
    }
    
    public void setEmailPessoal(String emailPessoal) {
        this.emailPessoal = emailPessoal;
    }
    
    public String getEmailCorporativo() {
        return emailCorporativo;
    }
    
    public void setEmailCorporativo(String emailCorporativo) {
        this.emailCorporativo = emailCorporativo;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getBairro() {
        return bairro;
    }
    
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getUf() {
        return uf;
    }
    
    public void setUf(String uf) {
        this.uf = uf;
    }
    
    public String getCep() {
        return cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }
    
    public String getNomeMunicipio() {
        return nomeMunicipio;
    }
    
    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }
    
    public String getSiglaOrgao() {
        return siglaOrgao;
    }
    
    public void setSiglaOrgao(String siglaOrgao) {
        this.siglaOrgao = siglaOrgao;
    }
    
    public String getCodigoIbge() {
        return codigoIbge;
    }
    
    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }
    
    public String getEnderecoCompletoUnidade() {
        return enderecoCompletoUnidade;
    }
    
    public void setEnderecoCompletoUnidade(String enderecoCompletoUnidade) {
        this.enderecoCompletoUnidade = enderecoCompletoUnidade;
    }
    
    public String getDescricaoEquipamento() {
        return descricaoEquipamento;
    }
    
    public void setDescricaoEquipamento(String descricaoEquipamento) {
        this.descricaoEquipamento = descricaoEquipamento;
    }
}
