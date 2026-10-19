package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="usuario")
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_usuario")
    private Long idUsuario;

    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_cliente",unique=true)
    private Cliente cliente;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_perfil",nullable=false)
    private Perfil perfil;

    @Column(nullable=false,unique=true,length=80)
    private String login;

    @Column(name="senha_hash",nullable=false,length=100)
    private String senhaHash;

    @Column(nullable=false)
    private Boolean ativo;

    @Column(name="data_cadastro",nullable=false,updatable=false)
    private LocalDateTime dataCadastro;

    @Column(name="ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    public Usuario() {}
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long value) { this.idUsuario = value; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente value) { this.cliente = value; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil value) { this.perfil = value; }
    public String getLogin() { return login; }
    public void setLogin(String value) { this.login = value; }
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String value) { this.senhaHash = value; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean value) { this.ativo = value; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime value) { this.dataCadastro = value; }
    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(LocalDateTime value) { this.ultimoAcesso = value; }
    @PrePersist public void prepararInclusao() { if (dataCadastro == null) dataCadastro = LocalDateTime.now(); if(ativo==null) ativo=true; }
}
