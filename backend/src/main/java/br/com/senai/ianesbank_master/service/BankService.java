package br.com.senai.ianesbank_master.service;

import br.com.senai.ianesbank_master.dto.*;
import br.com.senai.ianesbank_master.entity.*;
import br.com.senai.ianesbank_master.enums.*;
import br.com.senai.ianesbank_master.exception.ApiException;
import br.com.senai.ianesbank_master.repository.*;
import br.com.senai.ianesbank_master.util.Views;
import java.math.*;
import java.time.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class BankService {
 private final ContaRepository contas;private final TransacaoRepository transacoes;private final MovimentacaoRepository movimentos;private final UsuarioRepository usuarios;
 public BankService(ContaRepository c,TransacaoRepository t,MovimentacaoRepository m,UsuarioRepository u){contas=c;transacoes=t;movimentos=m;usuarios=u;}
 private ApiException erro(String mensagem){return new ApiException(HttpStatus.BAD_REQUEST,mensagem);}
 private Usuario usuario(String login){return usuarios.findByLogin(login).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Usuário não encontrado."));}
 private Conta bloquear(Long id){return contas.bloquearParaOperacao(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Conta não encontrada."));}
 private void validarConta(Conta c){if(c.getStatus()!=StatusConta.ATIVA || !Boolean.TRUE.equals(c.getCliente().getAtivo()))throw erro("Conta ou titular inativo/bloqueado.");}
 private void validarValor(BigDecimal valor){if(valor==null||valor.signum()<=0||valor.scale()>2||valor.precision()>15)throw erro("Informe valor positivo com até duas casas decimais.");}
 private void validarTitular(Conta c,String login){Usuario u=usuario(login);if(u.getCliente()==null || !u.getCliente().getIdCliente().equals(c.getCliente().getIdCliente()))throw new ApiException(HttpStatus.FORBIDDEN,"Essa conta não pertence ao usuário autenticado.");}
 private Transacao transacao(TipoTransacao tipo,Conta origem,Conta destino,BigDecimal valor,String descricao){
   Transacao t=new Transacao();t.setCodigo(UUID.randomUUID().toString());t.setTipo(tipo);t.setStatus(StatusTransacao.CONCLUIDA);t.setContaOrigem(origem);t.setContaDestino(destino);t.setValor(valor);t.setDescricao(descricao);return transacoes.save(t);
 }
 private void movimentar(Transacao t,Conta c,TipoMovimentacao tipo,BigDecimal valor){
   BigDecimal antes=c.getSaldo();BigDecimal depois=tipo==TipoMovimentacao.CREDITO?antes.add(valor):antes.subtract(valor);
   if(depois.signum()<0)throw erro("Saldo insuficiente para a operação.");
   c.setSaldo(depois);
   Movimentacao m=new Movimentacao();m.setTransacao(t);m.setConta(c);m.setTipo(tipo);m.setValor(valor);m.setSaldoAnterior(antes);m.setSaldoPosterior(depois);movimentos.save(m);
 }
 @Transactional
 public Map<String,Object> deposito(OperacaoForm f,String login){
   validarValor(f.valor());Conta c=bloquear(f.contaId());validarTitular(c,login);validarConta(c);
   Transacao t=transacao(TipoTransacao.DEPOSITO,null,c,f.valor(),f.descricao());movimentar(t,c,TipoMovimentacao.CREDITO,f.valor());return Views.transacao(t);
 }
 @Transactional
 public Map<String,Object> saque(OperacaoForm f,String login){
   validarValor(f.valor());Conta c=bloquear(f.contaId());validarTitular(c,login);validarConta(c);
   Transacao t=transacao(TipoTransacao.SAQUE,c,null,f.valor(),f.descricao());movimentar(t,c,TipoMovimentacao.DEBITO,f.valor());return Views.transacao(t);
 }
 @Transactional
 public Map<String,Object> transferencia(TransferenciaForm f,String login){
   validarValor(f.valor());Conta contaDestino=contas.findByNumero(f.numeroDestino().trim()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Conta de destino não encontrada."));
   Long idDestino=contaDestino.getIdConta(); if(f.contaOrigemId().equals(idDestino))throw erro("Selecione contas distintas.");
   // Ordem fixa de bloqueio reduz a possibilidade de deadlocks entre transferências simultâneas.
   Long low=Math.min(f.contaOrigemId(),idDestino);Long high=Math.max(f.contaOrigemId(),idDestino);
   Conta a=bloquear(low);Conta b=bloquear(high);
   Conta origem=f.contaOrigemId().equals(low)?a:b;Conta destino=idDestino.equals(low)?a:b;
   validarTitular(origem,login);validarConta(origem);validarConta(destino);
   if(origem.getSaldo().compareTo(f.valor())<0)throw erro("Saldo insuficiente para a transferência.");
   Transacao t=transacao(TipoTransacao.TRANSFERENCIA,origem,destino,f.valor(),f.descricao());
   movimentar(t,origem,TipoMovimentacao.DEBITO,f.valor());movimentar(t,destino,TipoMovimentacao.CREDITO,f.valor());
   return Views.transacao(t);
 }
 @Transactional(readOnly=true)
 public List<Map<String,Object>> minhasContas(String login){
   Usuario u=usuario(login);if(u.getCliente()==null)throw new ApiException(HttpStatus.FORBIDDEN,"Usuário sem cliente.");
   return contas.findByCliente_IdClienteOrderByIdContaDesc(u.getCliente().getIdCliente()).stream().map(Views::conta).toList();
 }
 @Transactional(readOnly=true)
 public Map<String,Object> contaDestino(String numero,String login){
   Conta destino=contas.findByNumero(numero.trim()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Conta de destino não encontrada."));
   validarConta(destino);Usuario u=usuario(login);
   boolean propria=u.getCliente()!=null&&u.getCliente().getIdCliente().equals(destino.getCliente().getIdCliente());
   return Views.m("id",destino.getIdConta(),"numero",destino.getNumero(),"titular",destino.getCliente().getNome(),"tipoConta",destino.getTipoConta().getDescricao(),"agenciaNumero",destino.getAgencia().getNumero(),"propria",propria);
 }
 @Transactional(readOnly=true)
 public Map<String,Object> meuPerfil(String login){Usuario u=usuario(login);return Views.m("usuario",Views.usuario(u),"cliente",u.getCliente()==null?null:Views.cliente(u.getCliente(),null));}
 @Transactional(readOnly=true)
 public List<Map<String,Object>> extrato(Long id,LocalDate inicio,LocalDate fim,String login){
   Conta c=contas.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Conta não encontrada."));validarTitular(c,login);
   if(inicio!=null && fim!=null && inicio.isAfter(fim))throw erro("Período inválido.");
   var lista=(inicio!=null||fim!=null)?movimentos.findByConta_IdContaAndDataHoraBetweenOrderByDataHoraDescIdMovimentacaoDesc(id,(inicio==null?LocalDate.of(2000,1,1):inicio).atStartOfDay(),(fim==null?LocalDate.of(9999,12,31):fim).atTime(LocalTime.MAX)):movimentos.findByConta_IdContaOrderByDataHoraDescIdMovimentacaoDesc(id);
   return lista.stream().map(Views::movimento).toList();
 }
 @Transactional(readOnly=true)
 public Map<String,Object> meuDashboard(String login){
   var meus=minhasContas(login);BigDecimal saldo=meus.stream().map(v->(BigDecimal)v.get("saldo")).reduce(BigDecimal.ZERO,BigDecimal::add);
   var recentes=meus.stream().flatMap(c->movimentos.findByConta_IdContaOrderByDataHoraDescIdMovimentacaoDesc((Long)c.get("id")).stream()).sorted(Comparator.comparing(Movimentacao::getDataHora).reversed()).limit(4).map(Views::movimento).toList();
   return Views.m("contas",meus.size(),"saldoTotal",saldo,"movimentacoes",recentes);
 }
}
