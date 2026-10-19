package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="cliente")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_cliente")
    private Long idCliente;

    @Column(nullable=false, length=150)
    private String nome;

    @Column(nullable=false, columnDefinition="CHAR(11)", unique=true)
    private String cpf;

    @Column(nullable=false, length=150)
    private String email;

    @Column(length=20)
    private String telefone;

    @Column(nullable=false)
    private Boolean ativo;

    @Column(name="data_cadastro", nullable=false, updatable=false)
    private LocalDateTime dataCadastro;

    public Cliente() {}
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long value) { this.idCliente = value; }
    public String getNome() { return nome; }
    public void setNome(String value) { this.nome = value; }
    public String getCpf() { return cpf; }
    public void setCpf(String value) { this.cpf = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String value) { this.telefone = value; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean value) { this.ativo = value; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime value) { this.dataCadastro = value; }
    @PrePersist public void prepararInclusao() { if (dataCadastro == null) dataCadastro = LocalDateTime.now(); if(ativo==null) ativo=true; }
}
