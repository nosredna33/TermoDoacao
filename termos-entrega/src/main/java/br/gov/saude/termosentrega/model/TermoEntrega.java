package br.gov.saude.termosentrega.model;

import java.time.LocalDateTime;

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
    
    public TermoEntrega() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
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
    
    public boolean isNovoMunicipio() {
        return novoMunicipio;
    }
    
    public void setNovoMunicipio(boolean novoMunicipio) {
        this.novoMunicipio = novoMunicipio;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
