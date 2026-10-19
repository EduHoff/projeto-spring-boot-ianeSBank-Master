package br.com.senai.ianesbank_master.util;

import br.com.senai.ianesbank_master.entity.*;
import java.util.LinkedHashMap;
import java.util.Map;
public final class Views {
 private Views() {}
 public static Map<String,Object> m(Object... pairs) {
   Map<String,Object> out = new LinkedHashMap<>();
   for(int i=0;i<pairs.length;i+=2) out.put((String)pairs[i],pairs[i+1]);
   return out;
 }
 public static Map<String,Object> cliente(Cliente c, Endereco e) {
   return m("id",c.getIdCliente(),"nome",c.getNome(),"cpf",c.getCpf(),"email",c.getEmail(),"telefone",c.getTelefone(),"ativo",c.getAtivo(),"dataCadastro",c.getDataCadastro(),"endereco",e==null?null:endereco(e));
 }
 public static Map<String,Object> endereco(Endereco e) {
   return m("id",e.getIdEndereco(),"cep",e.getCep(),"logradouro",e.getLogradouro(),"numero",e.getNumero(),"complemento",e.getComplemento(),"bairro",e.getBairro(),"cidade",e.getCidade(),"uf",e.getUf());
 }
 public static Map<String,Object> agencia(Agencia a) {return m("id",a.getIdAgencia(),"numero",a.getNumero(),"nome",a.getNome(),"cidade",a.getCidade(),"uf",a.getUf(),"ativa",a.getAtiva());}
 public static Map<String,Object> tipo(TipoConta t) {return m("id",t.getIdTipoConta(),"codigo",t.getCodigo(),"descricao",t.getDescricao(),"ativo",t.getAtivo());}
 public static Map<String,Object> conta(Conta c) {
   return m("id",c.getIdConta(),"numero",c.getNumero(),"saldo",c.getSaldo(),"status",c.getStatus().name(),"dataAbertura",c.getDataAbertura(),"clienteId",c.getCliente().getIdCliente(),"clienteNome",c.getCliente().getNome(),"agenciaId",c.getAgencia().getIdAgencia(),"agenciaNumero",c.getAgencia().getNumero(),"tipoContaId",c.getTipoConta().getIdTipoConta(),"tipoConta",c.getTipoConta().getDescricao());
 }
 public static Map<String,Object> usuario(Usuario u) {
   return m("id",u.getIdUsuario(),"login",u.getLogin(),"perfil",u.getPerfil().getCodigo(),"clienteId",u.getCliente()==null?null:u.getCliente().getIdCliente(),"clienteNome",u.getCliente()==null?null:u.getCliente().getNome(),"ativo",u.getAtivo(),"ultimoAcesso",u.getUltimoAcesso());
 }
 public static Map<String,Object> movimento(Movimentacao m) {
   Transacao t=m.getTransacao();String descricao=t.getDescricao();
   if(t.getTipo()==br.com.senai.ianesbank_master.enums.TipoTransacao.TRANSFERENCIA){
     Conta outra=m.getTipo()==br.com.senai.ianesbank_master.enums.TipoMovimentacao.DEBITO?t.getContaDestino():t.getContaOrigem();
     String prefixo=m.getTipo()==br.com.senai.ianesbank_master.enums.TipoMovimentacao.DEBITO?"Transferência para ":"Transferência recebida de ";
     descricao=prefixo+outra.getCliente().getNome()+" · conta "+outra.getNumero()+(descricao==null||descricao.isBlank()?"":" — "+descricao.trim());
   }
   return m("id",m.getIdMovimentacao(),"contaId",m.getConta().getIdConta(),"transacaoId",t.getIdTransacao(),"codigo",t.getCodigo(),"operacao",t.getTipo().name(),"tipo",m.getTipo().name(),"valor",m.getValor(),"saldoAnterior",m.getSaldoAnterior(),"saldoPosterior",m.getSaldoPosterior(),"dataHora",m.getDataHora(),"descricao",descricao);
 }
 public static Map<String,Object> transacao(Transacao t) {
   return m("id",t.getIdTransacao(),"codigo",t.getCodigo(),"tipo",t.getTipo().name(),"status",t.getStatus().name(),"valor",t.getValor(),"descricao",t.getDescricao(),"dataHora",t.getDataHora(),"contaOrigemId",t.getContaOrigem()==null?null:t.getContaOrigem().getIdConta(),"contaDestinoId",t.getContaDestino()==null?null:t.getContaDestino().getIdConta());
 }
}
