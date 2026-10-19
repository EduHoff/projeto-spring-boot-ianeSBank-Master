package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import br.com.senai.ianesbank_master.enums.TipoMovimentacao;

@Entity
@Table(name="movimentacao")
public class Movimentacao {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_movimentacao")
    private Long idMovimentacao;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_transacao",nullable=false)
    private Transacao transacao;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_conta",nullable=false)
    private Conta conta;

    @Enumerated(EnumType.STRING) @Column(nullable=false,length=10)
    private TipoMovimentacao tipo;

    @Column(nullable=false,precision=15,scale=2)
    private BigDecimal valor;

    @Column(name="saldo_anterior",nullable=false,precision=15,scale=2)
    private BigDecimal saldoAnterior;

    @Column(name="saldo_posterior",nullable=false,precision=15,scale=2)
    private BigDecimal saldoPosterior;

    @Column(name="data_hora",nullable=false,updatable=false)
    private LocalDateTime dataHora;

    public Movimentacao() {}
    public Long getIdMovimentacao() { return idMovimentacao; }
    public void setIdMovimentacao(Long value) { this.idMovimentacao = value; }
    public Transacao getTransacao() { return transacao; }
    public void setTransacao(Transacao value) { this.transacao = value; }
    public Conta getConta() { return conta; }
    public void setConta(Conta value) { this.conta = value; }
    public TipoMovimentacao getTipo() { return tipo; }
    public void setTipo(TipoMovimentacao value) { this.tipo = value; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal value) { this.valor = value; }
    public BigDecimal getSaldoAnterior() { return saldoAnterior; }
    public void setSaldoAnterior(BigDecimal value) { this.saldoAnterior = value; }
    public BigDecimal getSaldoPosterior() { return saldoPosterior; }
    public void setSaldoPosterior(BigDecimal value) { this.saldoPosterior = value; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime value) { this.dataHora = value; }
    @PrePersist public void prepararInclusao() { if (dataHora == null) dataHora = LocalDateTime.now(); }
}
