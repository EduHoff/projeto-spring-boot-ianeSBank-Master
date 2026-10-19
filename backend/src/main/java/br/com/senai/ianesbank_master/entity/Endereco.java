package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;

@Entity
@Table(name="endereco")
public class Endereco {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_endereco")
    private Long idEndereco;

    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_cliente",nullable=false,unique=true)
    private Cliente cliente;

    @Column(nullable=false,columnDefinition="CHAR(8)")
    private String cep;

    @Column(nullable=false,length=150)
    private String logradouro;

    @Column(nullable=false,length=15)
    private String numero;

    @Column(length=100)
    private String complemento;

    @Column(nullable=false,length=100)
    private String bairro;

    @Column(nullable=false,length=100)
    private String cidade;

    @Column(nullable=false,columnDefinition="CHAR(2)")
    private String uf;

    public Endereco() {}
    public Long getIdEndereco() { return idEndereco; }
    public void setIdEndereco(Long value) { this.idEndereco = value; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente value) { this.cliente = value; }
    public String getCep() { return cep; }
    public void setCep(String value) { this.cep = value; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String value) { this.logradouro = value; }
    public String getNumero() { return numero; }
    public void setNumero(String value) { this.numero = value; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String value) { this.complemento = value; }
    public String getBairro() { return bairro; }
    public void setBairro(String value) { this.bairro = value; }
    public String getCidade() { return cidade; }
    public void setCidade(String value) { this.cidade = value; }
    public String getUf() { return uf; }
    public void setUf(String value) { this.uf = value; }
}
