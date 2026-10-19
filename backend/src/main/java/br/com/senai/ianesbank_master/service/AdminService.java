package br.com.senai.ianesbank_master.service;

import br.com.senai.ianesbank_master.dto.*;
import br.com.senai.ianesbank_master.entity.*;
import br.com.senai.ianesbank_master.enums.*;
import br.com.senai.ianesbank_master.exception.ApiException;
import br.com.senai.ianesbank_master.repository.*;
import br.com.senai.ianesbank_master.util.Views;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AdminService {
 private final ClienteRepository clientes;private final EnderecoRepository enderecos;private final AgenciaRepository agencias;
 private final TipoContaRepository tipos;private final ContaRepository contas;private final UsuarioRepository usuarios;
 private final PerfilRepository perfis;private final TransacaoRepository transacoes;private final PasswordEncoder encoder;
 public AdminService(ClienteRepository cl,EnderecoRepository en,AgenciaRepository ag,TipoContaRepository ti,ContaRepository co,UsuarioRepository us,PerfilRepository pe,TransacaoRepository tr,PasswordEncoder enc){
 clientes=cl;enderecos=en;agencias=ag;tipos=ti;contas=co;usuarios=us;perfis=pe;transacoes=tr;encoder=enc;
 }
 private ApiException naoExiste(String descricao){return new ApiException(HttpStatus.NOT_FOUND,descricao+" não encontrado(a).");}
 private Cliente cliente(Long id){return clientes.findById(id).orElseThrow(()->naoExiste("Cliente"));}
 private Agencia agencia(Long id){return agencias.findById(id).orElseThrow(()->naoExiste("Agência"));}
 private TipoConta tipo(Long id){return tipos.findById(id).orElseThrow(()->naoExiste("Tipo de conta"));}
 private Conta conta(Long id){return contas.findById(id).orElseThrow(()->naoExiste("Conta"));}
 private Usuario usuario(Long id){return usuarios.findById(id).orElseThrow(()->naoExiste("Usuário"));}
 private Map<String,Object> clienteView(Cliente c){return Views.cliente(c,enderecos.findByCliente_IdCliente(c.getIdCliente()).orElse(null));}
 @Transactional(readOnly=true) public List<Map<String,Object>> listarClientes(){return clientes.findAll().stream().map(this::clienteView).toList();}
 @Transactional(readOnly=true) public Map<String,Object> consultarCliente(Long id){return clienteView(cliente(id));}
 @Transactional public Map<String,Object> salvarCliente(Long id,ClienteForm f){
   Cliente c=id==null?new Cliente():cliente(id);
   c.setNome(f.nome().trim());c.setCpf(f.cpf().trim());c.setEmail(f.email().trim());c.setTelefone(f.telefone());
   if(f.ativo()!=null)c.setAtivo(f.ativo());else if(id==null)c.setAtivo(true);
   clientes.saveAndFlush(c);
   if(f.endereco()!=null){EnderecoForm a=f.endereco();Endereco e=enderecos.findByCliente_IdCliente(c.getIdCliente()).orElseGet(Endereco::new);
     e.setCliente(c);e.setCep(a.cep().replaceAll("\\D",""));e.setLogradouro(a.logradouro());e.setNumero(a.numero());e.setComplemento(a.complemento());e.setBairro(a.bairro());e.setCidade(a.cidade());e.setUf(a.uf().toUpperCase());enderecos.save(e);
   }
   return clienteView(c);
 }
 @Transactional public void inativarCliente(Long id){Cliente c=cliente(id);c.setAtivo(false);}
 @Transactional(readOnly=true) public List<Map<String,Object>> listarAgencias(){return agencias.findAll().stream().map(Views::agencia).toList();}
 @Transactional public Map<String,Object> salvarAgencia(Long id,AgenciaForm f){
   Agencia a=id==null?new Agencia():agencia(id);a.setNumero(f.numero().trim());a.setNome(f.nome());a.setCidade(f.cidade());a.setUf(f.uf().toUpperCase());
   if(f.ativa()!=null)a.setAtiva(f.ativa());else if(id==null)a.setAtiva(true);return Views.agencia(agencias.save(a));
 }
 @Transactional(readOnly=true) public List<Map<String,Object>> listarTipos(){return tipos.findAll().stream().map(Views::tipo).toList();}
 @Transactional public Map<String,Object> salvarTipo(Long id,TipoContaForm f){
   TipoConta t=id==null?new TipoConta():tipo(id);t.setCodigo(f.codigo().toUpperCase().trim());t.setDescricao(f.descricao());
   if(f.ativo()!=null)t.setAtivo(f.ativo());else if(id==null)t.setAtivo(true);return Views.tipo(tipos.save(t));
 }
 @Transactional(readOnly=true) public List<Map<String,Object>> listarContas(){return contas.findAll().stream().map(Views::conta).toList();}
 @Transactional public Map<String,Object> abrirConta(ContaForm f){
   Cliente c=cliente(f.clienteId());Agencia a=agencia(f.agenciaId());TipoConta t=tipo(f.tipoContaId());
   if(!Boolean.TRUE.equals(c.getAtivo())||!Boolean.TRUE.equals(a.getAtiva())||!Boolean.TRUE.equals(t.getAtivo()))throw new ApiException(HttpStatus.BAD_REQUEST,"Cliente, agência e tipo de conta precisam estar ativos.");
   Conta co=new Conta();co.setCliente(c);co.setAgencia(a);co.setTipoConta(t);co.setSaldo(BigDecimal.ZERO);co.setStatus(StatusConta.ATIVA);
   co.setNumero("SB"+UUID.randomUUID().toString().replace("-","").substring(0,12).toUpperCase());
   return Views.conta(contas.saveAndFlush(co));
 }
 @Transactional public Map<String,Object> alterarStatus(Long id,ContaStatusForm f){
   Conta c=conta(id);StatusConta status;
   try{status=StatusConta.valueOf(f.status().toUpperCase());}catch(Exception e){throw new ApiException(HttpStatus.BAD_REQUEST,"Status inválido.");}
   if(status==StatusConta.ENCERRADA && c.getSaldo().compareTo(BigDecimal.ZERO)!=0)throw new ApiException(HttpStatus.BAD_REQUEST,"A conta precisa estar com saldo zero antes do encerramento.");
   if(c.getStatus()==StatusConta.ENCERRADA && status!=StatusConta.ENCERRADA)throw new ApiException(HttpStatus.BAD_REQUEST,"Conta encerrada não pode ser reativada.");
   c.setStatus(status);return Views.conta(contas.save(c));
 }
 @Transactional(readOnly=true) public List<Map<String,Object>> listarUsuarios(){return usuarios.findAll().stream().map(Views::usuario).toList();}
 @Transactional public Map<String,Object> salvarUsuario(Long id,UsuarioForm f){
   Usuario u=id==null?new Usuario():usuario(id);
   Perfil perfil=perfis.findByCodigo(f.perfil().toUpperCase()).orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST,"Perfil inválido."));
   if("CLIENTE".equals(perfil.getCodigo())){
     if(f.clienteId()==null)throw new ApiException(HttpStatus.BAD_REQUEST,"Usuário CLIENTE precisa de um cliente vinculado.");
     u.setCliente(cliente(f.clienteId()));
   } else u.setCliente(null);
   u.setPerfil(perfil);u.setLogin(f.login().trim());
   if(f.senha()!=null&&!f.senha().isBlank()){
     if(f.senha().length()<8)throw new ApiException(HttpStatus.BAD_REQUEST,"Senha deve ter pelo menos 8 caracteres.");
     u.setSenhaHash(encoder.encode(f.senha()));
   } else if(id==null)throw new ApiException(HttpStatus.BAD_REQUEST,"Senha é obrigatória ao criar usuário.");
   if(f.ativo()!=null)u.setAtivo(f.ativo());else if(id==null)u.setAtivo(true);
   return Views.usuario(usuarios.saveAndFlush(u));
 }
 @Transactional(readOnly=true) public Map<String,Object> dashboard(){
   BigDecimal total=contas.findAll().stream().map(Conta::getSaldo).reduce(BigDecimal.ZERO,BigDecimal::add);
   return Views.m("clientes",clientes.count(),"contas",contas.count(),"agencias",agencias.count(),"transacoes",transacoes.count(),"saldoTotal",total);
 }
}
