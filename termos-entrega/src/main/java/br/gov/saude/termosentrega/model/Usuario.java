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
public class Usuario {
    
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String role;
    private boolean ativo;
    private String tokenRecuperacaoSenha;
    private LocalDateTime tokenExpiracao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Usuario(Long id, String nome, String email, String senha, String role, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.ativo = ativo;
    }    
   
}
