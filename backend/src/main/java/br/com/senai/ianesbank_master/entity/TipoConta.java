package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;

@Entity
@Table(name="tipo_conta")
public class TipoConta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_tipo_conta")
    private Long idTipoConta;

    @Column(nullable=false,length=30,unique=true)
    private String codigo;

    @Column(nullable=false,length=100)
    private String descricao;

    @Column(nullable=false)
    private Boolean ativo;

    public TipoConta() {}
    public Long getIdTipoConta() { return idTipoConta; }
    public void setIdTipoConta(Long value) { this.idTipoConta = value; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String value) { this.codigo = value; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String value) { this.descricao = value; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean value) { this.ativo = value; }
}
