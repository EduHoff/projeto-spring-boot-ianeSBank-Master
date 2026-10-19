package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import br.com.senai.ianesbank_master.enums.StatusConta;

@Entity
@Table(name="conta")
public class Conta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_conta")
    private Long idConta;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_cliente",nullable=false)
    private Cliente cliente;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_agencia",nullable=false)
    private Agencia agencia;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_tipo_conta",nullable=false)
    private TipoConta tipoConta;

    @Column(nullable=false,length=20,unique=true)
    private String numero;

    @Column(nullable=false,precision=15,scale=2)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20)
    private StatusConta status;

    @Version @Column(nullable=false)
    private Long versao;

    @Column(name="data_abertura",nullable=false,updatable=false)
    private LocalDateTime dataAbertura;

    public Conta() {}
    public Long getIdConta() { return idConta; }
    public void setIdConta(Long value) { this.idConta = value; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente value) { this.cliente = value; }
    public Agencia getAgencia() { return agencia; }
    public void setAgencia(Agencia value) { this.agencia = value; }
    public TipoConta getTipoConta() { return tipoConta; }
    public void setTipoConta(TipoConta value) { this.tipoConta = value; }
    public String getNumero() { return numero; }
    public void setNumero(String value) { this.numero = value; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal value) { this.saldo = value; }
    public StatusConta getStatus() { return status; }
    public void setStatus(StatusConta value) { this.status = value; }
    public Long getVersao() { return versao; }
    public void setVersao(Long value) { this.versao = value; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime value) { this.dataAbertura = value; }
    @PrePersist public void prepararInclusao() { if (dataAbertura == null) dataAbertura = LocalDateTime.now(); if(saldo==null) saldo=BigDecimal.ZERO; if(status==null) status=StatusConta.ATIVA; }
}
