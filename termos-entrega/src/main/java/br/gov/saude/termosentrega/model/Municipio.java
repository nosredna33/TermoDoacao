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
public class Municipio {
    
    private Long id;
    private String nome;
    private String nomeNormalizado;
    private String uf;
    private String codigoIbge;
    private boolean ativo;
    private LocalDateTime dataCriacao;

    public Municipio(String nome, String nomeNormalizado, String uf, String codigoIbge) {
        this.nome = nome;
        this.nomeNormalizado = nomeNormalizado;
        this.uf = uf;
        this.codigoIbge = codigoIbge;
        this.ativo = true;
    }
    
  
}
