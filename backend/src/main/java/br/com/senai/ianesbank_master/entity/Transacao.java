package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import br.com.senai.ianesbank_master.enums.StatusTransacao;
import br.com.senai.ianesbank_master.enums.TipoTransacao;

@Entity
@Table(name="transacao")
public class Transacao {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_transacao")
    private Long idTransacao;

    @Column(nullable=false,length=36,unique=true)
    private String codigo;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_conta_origem")
    private Conta contaOrigem;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_conta_destino")
    private Conta contaDestino;

    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20)
    private TipoTransacao tipo;

    @Column(nullable=false,precision=15,scale=2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20)
    private StatusTransacao status;

    @Column(length=255)
    private String descricao;

    @Column(name="data_hora",nullable=false,updatable=false)
    private LocalDateTime dataHora;

    public Transacao() {}
    public Long getIdTransacao() { return idTransacao; }
    public void setIdTransacao(Long value) { this.idTransacao = value; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String value) { this.codigo = value; }
    public Conta getContaOrigem() { return contaOrigem; }
    public void setContaOrigem(Conta value) { this.contaOrigem = value; }
    public Conta getContaDestino() { return contaDestino; }
    public void setContaDestino(Conta value) { this.contaDestino = value; }
    public TipoTransacao getTipo() { return tipo; }
    public void setTipo(TipoTransacao value) { this.tipo = value; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal value) { this.valor = value; }
    public StatusTransacao getStatus() { return status; }
    public void setStatus(StatusTransacao value) { this.status = value; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String value) { this.descricao = value; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime value) { this.dataHora = value; }
    @PrePersist public void prepararInclusao() { if (dataHora == null) dataHora = LocalDateTime.now(); if(status==null) status=StatusTransacao.CONCLUIDA; }
}
