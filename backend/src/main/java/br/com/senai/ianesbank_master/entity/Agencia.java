package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;

@Entity
@Table(name="agencia")
public class Agencia {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_agencia")
    private Long idAgencia;

    @Column(nullable=false,length=10,unique=true)
    private String numero;

    @Column(nullable=false,length=100)
    private String nome;

    @Column(nullable=false,length=100)
    private String cidade;

    @Column(nullable=false,columnDefinition="CHAR(2)")
    private String uf;

    @Column(nullable=false)
    private Boolean ativa;

    public Agencia() {}
    public Long getIdAgencia() { return idAgencia; }
    public void setIdAgencia(Long value) { this.idAgencia = value; }
    public String getNumero() { return numero; }
    public void setNumero(String value) { this.numero = value; }
    public String getNome() { return nome; }
    public void setNome(String value) { this.nome = value; }
    public String getCidade() { return cidade; }
    public void setCidade(String value) { this.cidade = value; }
    public String getUf() { return uf; }
    public void setUf(String value) { this.uf = value; }
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean value) { this.ativa = value; }
}
