package br.gov.saude.termosentrega.model;

import java.time.LocalDateTime;

public class Municipio {
    
    private Long id;
    private String nome;
    private String nomeNormalizado;
    private String uf;
    private String codigoIbge;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    
    public Municipio() {
    }
    
    public Municipio(String nome, String nomeNormalizado, String uf, String codigoIbge) {
        this.nome = nome;
        this.nomeNormalizado = nomeNormalizado;
        this.uf = uf;
        this.codigoIbge = codigoIbge;
        this.ativo = true;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getNomeNormalizado() {
        return nomeNormalizado;
    }
    
    public void setNomeNormalizado(String nomeNormalizado) {
        this.nomeNormalizado = nomeNormalizado;
    }
    
    public String getUf() {
        return uf;
    }
    
    public void setUf(String uf) {
        this.uf = uf;
    }
    
    public String getCodigoIbge() {
        return codigoIbge;
    }
    
    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
