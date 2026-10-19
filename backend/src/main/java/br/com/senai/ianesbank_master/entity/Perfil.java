package br.com.senai.ianesbank_master.entity;

import jakarta.persistence.*;

@Entity
@Table(name="perfil")
public class Perfil {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_perfil")
    private Long idPerfil;

    @Column(nullable=false,length=20,unique=true)
    private String codigo;

    @Column(nullable=false,length=100)
    private String descricao;

    public Perfil() {}
    public Long getIdPerfil() { return idPerfil; }
    public void setIdPerfil(Long value) { this.idPerfil = value; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String value) { this.codigo = value; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String value) { this.descricao = value; }
}
