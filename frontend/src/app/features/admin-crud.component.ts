import { Component,inject,OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../core/api.service';
import { IconComponent } from '../shared/icon.component';
const configs:Record<string,any>={
  'clientes':{title:'Clientes',subtitle:'Cadastre, altere e inative clientes e seus endereços.',columns:[['nome','Nome'],['cpf','CPF'],['email','E-mail'],['ativo','Ativo']],fields:[
    {key:'nome',label:'Nome completo',required:true},{key:'cpf',label:'CPF (11 dígitos)',required:true},{key:'email',label:'E-mail',required:true},{key:'telefone',label:'Telefone'},
    {key:'cep',label:'CEP (8 dígitos)'},{key:'logradouro',label:'Logradouro'},{key:'numero',label:'Número'},{key:'complemento',label:'Complemento'},
    {key:'bairro',label:'Bairro'},{key:'cidade',label:'Cidade'},{key:'uf',label:'UF'}, {key:'ativo',label:'Cliente ativo',type:'check'}]},
    'agencias':{title:'Agências',subtitle:'Gerencie a rede de agências e pontos de atendimento.',columns:[['numero','Número'],['nome','Nome'],['cidade','Cidade'],['ativa','Ativa']],fields:[
      {key:'numero',label:'Número',required:true},{key:'nome',label:'Nome',required:true},{key:'cidade',label:'Cidade',required:true},{key:'uf',label:'UF',required:true},{key:'ativa',label:'Agência ativa',type:'check'}]},
      'tipos-conta':{title:'Tipos de conta',subtitle:'Cadastre e configure as modalidades de conta oferecidas.',columns:[['codigo','Código'],['descricao','Descrição'],['ativo','Ativo']],fields:[
        {key:'codigo',label:'Código',required:true},{key:'descricao',label:'Descrição',required:true},{key:'ativo',label:'Tipo ativo',type:'check'}]},
        'contas':{title:'Contas bancárias',subtitle:'Abra novas contas e controle o status de cada uma.',columns:[['numero','Conta'],['clienteNome','Titular'],['agenciaNumero','Agência'],['tipoConta','Tipo'],['saldo','Saldo'],['status','Status']],fields:[
          {key:'clienteId',label:'Cliente',type:'select',ref:'clientes',required:true},{key:'agenciaId',label:'Agência',type:'select',ref:'agencias',required:true},{key:'tipoContaId',label:'Tipo de conta',type:'select',ref:'tipos-conta',required:true}]},
          'usuarios':{title:'Usuários',subtitle:'Controle os logins do administrador e dos clientes.',columns:[['login','Login'],['perfil','Perfil'],['clienteNome','Cliente'],['ativo','Ativo']],fields:[
            {key:'login',label:'Login',required:true},{key:'senha',label:'Senha (deixe em branco na edição)',type:'password'},
            {key:'perfil',label:'Perfil',type:'enum',options:[['ADMIN','Administrador'],['CLIENTE','Cliente']],required:true},
            {key:'clienteId',label:'Cliente vinculado',type:'select',ref:'clientes'},{key:'ativo',label:'Usuário ativo',type:'check'}]}
};
@Component({standalone:true,imports:[CommonModule,FormsModule,IconComponent],template:`
  <div class="page-title crud-title"><div><div class="eyebrow">{{recurso==='usuarios'?'SEGURANÇA':'CADASTROS'}}</div><h1>{{cfg.title}}</h1><p class="muted">{{cfg.subtitle}}</p></div><button class="btn primary" (click)="novo()"><app-icon name="plus"/> {{novoTexto}}</button></div>
  @if(erro){<div class="alert error">{{erro}}</div>} @if(sucesso){<div class="alert success">{{sucesso}}</div>}
  <div class="panel data-panel"><div class="table-header"><h2>{{painelTitulo}} <span class="count">{{rows.length}}</span></h2><div class="table-tools"><div class="search-wrap"><span><app-icon name="search"/></span><input class="search" [placeholder]="buscaPlaceholder" [(ngModel)]="busca"></div>@if(recurso==='clientes'){<button type="button" class="filter-button"><app-icon name="filter"/> Filtros</button>}</div></div>
  <div class="table-wrap">@if(recurso==='clientes'){
    <table class="clients-table"><thead><tr><th>Cliente</th><th>CPF</th><th>E-mail</th><th>Status</th><th>Ações</th></tr></thead><tbody>
    @for(row of filtrados;track row.id){<tr><td><span class="client-cell"><i>{{(row.nome||'?').substring(0,1).toUpperCase()}}</i><b>{{nomeCurto(row.nome)}}</b></span></td><td>{{cpfFormatado(row.cpf)}}</td><td>{{row.email||'–'}}</td><td><span class="client-status" [class.inactive]="!row.ativo"><i></i>{{row.ativo?'Ativo':'Inativo'}}</span></td><td class="table-actions"><button class="edit-action" (click)="editar(row)"><app-icon name="edit"/> Editar</button>@if(row.ativo){<button class="danger-text" (click)="inativar(row)"><app-icon name="ban"/> Inativar</button>}</td></tr>
    }@empty{<tr><td colspan="5" class="empty">Nenhum cliente encontrado.</td></tr>}</tbody></table>
  } @else if(recurso==='contas') {
    <table class="clients-table resource-table"><thead><tr><th>Conta</th><th>Titular</th><th>Agência</th><th>Tipo</th><th>Saldo</th><th>Status</th><th>Ações</th></tr></thead><tbody>
    @for(row of filtrados;track row.id){<tr><td><b>{{row.numero}}</b></td><td><span class="client-cell"><i>{{inicial(row.clienteNome)}}</i><b>{{nomeCurto(row.clienteNome)}}</b></span></td><td>{{row.agenciaNumero}}</td><td>{{row.tipoConta}}</td><td><b class="money-cell">{{format(row.saldo,'saldo')}}</b></td><td><span class="client-status" [class.inactive]="row.status!=='ATIVA'"><i></i>{{statusConta(row.status)}}</span></td><td class="table-actions"><button class="danger-text" (click)="bloquear(row)"><app-icon name="lock"/> Bloquear</button></td></tr>}@empty{<tr><td colspan="7" class="empty">Nenhuma conta encontrada.</td></tr>}</tbody></table>
  } @else if(recurso==='agencias') {
    <table class="clients-table resource-table"><thead><tr><th>Agência</th><th>Nome</th><th>Cidade</th><th>Status</th><th>Ações</th></tr></thead><tbody>
    @for(row of filtrados;track row.id){<tr><td><span class="code-pill">{{row.numero}}</span></td><td><b>{{row.nome}}</b></td><td><span class="location-cell"><app-icon name="location"/> {{row.cidade}} · {{row.uf}}</span></td><td><span class="client-status" [class.inactive]="!row.ativa"><i></i>{{row.ativa?'Ativa':'Inativa'}}</span></td><td class="table-actions"><button (click)="editar(row)"><app-icon name="edit"/> Editar</button></td></tr>}@empty{<tr><td colspan="5" class="empty">Nenhuma agência encontrada.</td></tr>}</tbody></table>
  } @else if(recurso==='tipos-conta') {
    <table class="clients-table resource-table"><thead><tr><th>Código</th><th>Descrição</th><th>Status</th><th>Ações</th></tr></thead><tbody>
    @for(row of filtrados;track row.id){<tr><td><span class="code-pill neutral">{{row.codigo}}</span></td><td><span class="description-cell"><i><app-icon name="card"/></i><b>{{row.descricao}}</b></span></td><td><span class="client-status" [class.inactive]="!row.ativo"><i></i>{{row.ativo?'Ativo':'Inativo'}}</span></td><td class="table-actions"><button (click)="editar(row)"><app-icon name="edit"/> Editar</button></td></tr>}@empty{<tr><td colspan="4" class="empty">Nenhum tipo encontrado.</td></tr>}</tbody></table>
  } @else if(recurso==='usuarios') {
    <table class="clients-table resource-table"><thead><tr><th>Login</th><th>Perfil</th><th>Cliente vinculado</th><th>Status</th><th>Ações</th></tr></thead><tbody>
    @for(row of filtrados;track row.id){<tr><td><span class="client-cell"><i [class.admin-avatar]="row.perfil==='ADMIN'">{{inicial(row.login)}}</i><b>{{row.login}}</b></span></td><td><span class="profile-badge" [class.admin]="row.perfil==='ADMIN'">{{row.perfil==='ADMIN'?'Administrador':'Cliente'}}</span></td><td>{{row.clienteNome||'–'}}</td><td><span class="client-status" [class.inactive]="!row.ativo"><i></i>{{row.ativo?'Ativo':'Inativo'}}</span></td><td class="table-actions"><button (click)="editar(row)"><app-icon name="edit"/> Editar</button></td></tr>}@empty{<tr><td colspan="5" class="empty">Nenhum usuário encontrado.</td></tr>}</tbody></table>
  }</div><div class="table-footer"><span>Mostrando {{filtrados.length}} de {{rows.length}} {{plural}}</span>@if(recurso==='contas'){<span>{{bloqueadas===0?'Nenhuma conta bloqueada':bloqueadas+' conta(s) bloqueada(s)'}}</span>}@else if(recurso==='clientes'){<div><button disabled>‹</button><button class="current">1</button><button disabled>›</button></div>}</div></div>
  @if(editando){<div class="modal-backdrop" (click)="fechar()"><div class="modal client-modal resource-modal" (click)="$event.stopPropagation()"><div class="modal-header">
    @if(recurso==='clientes'){
      @if(form.id){<div class="edit-client-heading"><span>{{(form.nome||'?').substring(0,1).toUpperCase()}}</span><div><div class="eyebrow">EDITAR CLIENTE</div><h2>{{nomeCurto(form.nome)}}</h2><small>CPF {{cpfFormatado(form.cpf)}}</small></div></div>}
      @else{<div><div class="eyebrow">NOVO CADASTRO</div><h2>Cadastrar cliente</h2><small>Campos com <b class="required">*</b> são obrigatórios.</small></div>}
    } @else {<div><div class="eyebrow">{{modalEyebrow}}</div><h2>{{modalTitulo}}</h2><small>{{modalSubtitulo}}</small></div>}
    <button class="close" (click)="fechar()">×</button></div>
    @if(recurso==='clientes'){
      <form class="client-form" (ngSubmit)="salvar()" #fCliente="ngForm">
      <div class="form-section-title"><app-icon name="user"/> DADOS PESSOAIS</div><div class="client-form-grid">
      <div class="col-3"><label>Nome completo<span class="required">*</span></label><input [(ngModel)]="form.nome" name="nome" required placeholder="Ex.: Maria da Silva"></div>
      <div class="col-1-half"><label>CPF<span class="required">*</span></label><input [(ngModel)]="form.cpf" name="cpf" required placeholder="000.000.000–00"></div><div class="col-1-half"><label>Telefone</label><input [(ngModel)]="form.telefone" name="telefone" placeholder="(00) 00000–0000"></div>
      <div class="col-3"><label>E-mail<span class="required">*</span></label><input type="email" [(ngModel)]="form.email" name="email" required placeholder="nome@email.com"></div>
      </div>
      <div class="form-section-title address-title"><app-icon name="location"/> ENDEREÇO</div><div class="client-form-grid address-grid">
      <div class="col-1"><label>CEP</label><input [(ngModel)]="form.cep" name="cep" placeholder="00000–000"></div><div class="col-2"><label>Logradouro</label><input [(ngModel)]="form.logradouro" name="logradouro" placeholder="Rua, avenida..."></div>
      <div class="col-1"><label>Número</label><input [(ngModel)]="form.numero" name="numero" placeholder="Nº"></div><div class="col-2"><label>Complemento</label><input [(ngModel)]="form.complemento" name="complemento" placeholder="Apto, bloco, sala..."></div>
      <div class="col-1"><label>Bairro</label><input [(ngModel)]="form.bairro" name="bairro" placeholder="Bairro"></div><div class="col-1"><label>Cidade</label><input [(ngModel)]="form.cidade" name="cidade" placeholder="Cidade"></div><div class="col-1"><label>UF</label><select [(ngModel)]="form.uf" name="uf"><option value="">Selecione</option><option>AC</option><option>AL</option><option>AP</option><option>AM</option><option>BA</option><option>CE</option><option>DF</option><option>ES</option><option>GO</option><option>MA</option><option>MT</option><option>MS</option><option>MG</option><option>PA</option><option>PB</option><option>PR</option><option>PE</option><option>PI</option><option>RJ</option><option>RN</option><option>RS</option><option>RO</option><option>RR</option><option>SC</option><option>SP</option><option>SE</option><option>TO</option></select></div>
      </div>
      <label class="active-toggle"><span><b>Cliente ativo</b><small>Clientes inativos não podem abrir novas contas.</small></span><input type="checkbox" [(ngModel)]="form.ativo" name="ativo"><i></i></label>
      @if(erroModal){<div class="alert error">{{erroModal}}</div>}<div class="modal-actions"><button type="button" class="btn secondary" (click)="fechar()">Cancelar</button><button type="submit" class="btn primary" [disabled]="salvando||fCliente.invalid">{{salvando?'Salvando...':(form.id?'Salvar alterações':'Cadastrar cliente')}}</button></div>
      </form>
    } @else {
      <form class="client-form resource-form" (ngSubmit)="salvar()" #fCadastro="ngForm">
      @if(recurso==='contas'){
        <div class="resource-grid"><div class="full"><label>Titular<span class="required">*</span></label><select [(ngModel)]="form.clienteId" name="clienteId" required><option [ngValue]="null">Selecione o cliente</option>@for(opt of referencias['clientes']||[];track opt.id){<option [ngValue]="opt.id">{{opt.nome}}</option>}</select></div><div><label>Agência<span class="required">*</span></label><select [(ngModel)]="form.agenciaId" name="agenciaId" required><option [ngValue]="null">Selecione</option>@for(opt of referencias['agencias']||[];track opt.id){<option [ngValue]="opt.id">{{opt.numero}} · {{opt.nome}}</option>}</select></div><div><label>Tipo de conta<span class="required">*</span></label><select [(ngModel)]="form.tipoContaId" name="tipoContaId" required><option [ngValue]="null">Selecione</option>@for(opt of referencias['tipos-conta']||[];track opt.id){<option [ngValue]="opt.id">{{opt.descricao}}</option>}</select></div></div>
        <div class="account-summary"><i><app-icon name="card"/></i><span><small>RESUMO</small><b>{{refNome('clientes',form.clienteId)||'Titular não selecionado'}}</b><em>{{refNome('tipos-conta',form.tipoContaId)||'Tipo de conta'}} · {{refNome('agencias',form.agenciaId)||'Agência'}}</em></span></div>
      } @else if(recurso==='agencias'){
        <div class="resource-grid"><div><label>Número<span class="required">*</span></label><input [(ngModel)]="form.numero" name="numero" required placeholder="0000"></div><div><label>Nome da agência<span class="required">*</span></label><input [(ngModel)]="form.nome" name="nome" required placeholder="Ex.: Agência ianeS Curitiba"></div><div class="wide"><label>Cidade<span class="required">*</span></label><input [(ngModel)]="form.cidade" name="cidade" required placeholder="Cidade"></div><div class="narrow"><label>UF<span class="required">*</span></label><select [(ngModel)]="form.uf" name="uf" required><option value="">Selecione</option>@for(uf of ufs;track uf){<option [value]="uf">{{uf}}</option>}</select></div></div><label class="active-toggle"><span><b>Agência ativa</b><small>Só agências ativas aparecem na abertura de contas.</small></span><input type="checkbox" [(ngModel)]="form.ativa" name="ativa"><i></i></label>
      } @else if(recurso==='tipos-conta'){
        <div class="resource-grid"><div><label>Código<span class="required">*</span></label><input [(ngModel)]="form.codigo" name="codigo" required placeholder="EX.: SALARIO"><small class="field-help">Sem espaços ou acentos.</small></div><div><label>Descrição<span class="required">*</span></label><input [(ngModel)]="form.descricao" name="descricao" required placeholder="Ex.: Conta salário"><small class="field-help">Nome exibido para o cliente.</small></div></div><label class="active-toggle"><span><b>Tipo ativo</b><small>Tipos inativos não podem ser usados em novas contas.</small></span><input type="checkbox" [(ngModel)]="form.ativo" name="ativo"><i></i></label>
      } @else if(recurso==='usuarios'){
        <div class="resource-grid"><div><label>Login<span class="required">*</span></label><input [(ngModel)]="form.login" name="login" required placeholder="Ex.: maria.silva"></div><div><label>Senha inicial<span class="required">{{form.id?'':'*'}}</span></label><input type="password" [(ngModel)]="form.senha" name="senha" [required]="!form.id" placeholder="Mínimo de 8 caracteres"></div></div><label>Perfil de acesso<span class="required">*</span></label><div class="profile-options"><button type="button" [class.selected]="form.perfil==='CLIENTE'" (click)="form.perfil='CLIENTE'"><i><app-icon name="user"/></i><span><b>Cliente</b><small>Acessa só as próprias contas</small></span></button><button type="button" [class.selected]="form.perfil==='ADMIN'" (click)="form.perfil='ADMIN';form.clienteId=null"><i><app-icon name="shield"/></i><span><b>Administrador</b><small>Acesso total à gestão</small></span></button></div>@if(form.perfil==='CLIENTE'){<label>Cliente vinculado<span class="required">*</span></label><select [(ngModel)]="form.clienteId" name="clienteId" required><option [ngValue]="null">Selecione o cliente</option>@for(opt of referencias['clientes']||[];track opt.id){<option [ngValue]="opt.id">{{opt.nome}}</option>}</select>}<label class="active-toggle"><span><b>Usuário ativo</b><small>Usuários inativos não conseguem entrar no sistema.</small></span><input type="checkbox" [(ngModel)]="form.ativo" name="ativo"><i></i></label>
      }
      @if(erroModal){<div class="alert error">{{erroModal}}</div>}<div class="modal-actions"><button type="button" class="btn secondary" (click)="fechar()">Cancelar</button><button type="submit" class="btn primary" [disabled]="salvando||fCadastro.invalid">{{salvando?'Salvando...':submitTexto}}</button></div></form>
    }</div></div>}
    `})
export class AdminCrudComponent implements OnInit {
  api=inject(ApiService);route=inject(ActivatedRoute);recurso='clientes';cfg:any=configs['clientes'];rows:any[]=[];busca='';erro='';sucesso='';editando=false;form:any={};salvando=false;erroModal='';referencias:Record<string,any[]>={};
  ufs=['AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'];
  ngOnInit(){this.route.data.subscribe(d=>{this.recurso=d['recurso']||'clientes';this.cfg=configs[this.recurso];this.busca='';this.carregar();this.refs();});}
  get novoTexto(){return ({clientes:'Novo cliente',contas:'Abrir conta',agencias:'Nova agência','tipos-conta':'Novo tipo',usuarios:'Novo usuário'} as any)[this.recurso];}
  get painelTitulo(){return ({clientes:'Clientes cadastrados',contas:'Contas abertas',agencias:'Rede de agências','tipos-conta':'Modalidades',usuarios:'Usuários do sistema'} as any)[this.recurso];}
  get buscaPlaceholder(){return ({clientes:'Buscar por nome, CPF ou e-mail',contas:'Buscar por conta, titular ou agência',agencias:'Buscar por número, nome ou cidade','tipos-conta':'Buscar por código ou descrição',usuarios:'Buscar por login ou cliente'} as any)[this.recurso];}
  get plural(){return ({clientes:'clientes',contas:'contas',agencias:'agências','tipos-conta':'tipos de conta',usuarios:'usuários'} as any)[this.recurso];}
  get bloqueadas(){return this.rows.filter(r=>r.status==='BLOQUEADA').length;}
  get modalEyebrow(){if(this.form.id)return 'EDITAR CADASTRO';return this.recurso==='contas'?'NOVA CONTA':this.recurso==='usuarios'?'NOVO ACESSO':'NOVO CADASTRO';}
  get modalTitulo(){if(this.form.id)return ({agencias:'Editar agência','tipos-conta':'Editar tipo de conta',usuarios:'Editar usuário'} as any)[this.recurso]||this.cfg.title;return ({contas:'Abrir conta bancária',agencias:'Cadastrar agência','tipos-conta':'Cadastrar tipo de conta',usuarios:'Cadastrar usuário'} as any)[this.recurso];}
  get modalSubtitulo(){if(this.recurso==='contas')return 'O número da conta é gerado automaticamente.';if(this.recurso==='usuarios')return 'Defina o login, a senha inicial e o perfil de acesso.';return 'Campos com * são obrigatórios.';}
  get submitTexto(){if(this.form.id)return 'Salvar alterações';return ({contas:'Abrir conta',agencias:'Cadastrar agência','tipos-conta':'Cadastrar tipo',usuarios:'Cadastrar usuário'} as any)[this.recurso];}
  get filtrados(){const q=this.busca.toLowerCase().trim();return this.rows.filter(r=>!q||Object.values(r).some(x=>String(x??'').toLowerCase().includes(q)));}
  format(v:any,key:string){if(v===null||v===undefined)return '–';if(typeof v==='boolean')return v?'Sim':'Não';if(key==='saldo')return Number(v).toLocaleString('pt-BR',{style:'currency',currency:'BRL'});return v;}
  nomeCurto(nome:any){const partes=String(nome||'–').trim().split(/\s+/);return partes.length>1?partes[0]+' '+partes.at(-1)![0]+'.':partes[0];}
  inicial(valor:any){return String(valor||'?').substring(0,1).toUpperCase();}
  statusConta(status:any){return status==='ATIVA'?'Ativa':status==='BLOQUEADA'?'Bloqueada':'Encerrada';}
  refNome(tipo:string,id:any){const r=(this.referencias[tipo]||[]).find(x=>x.id===id);return r?(r.nome||r.descricao||r.numero):'';}
  cpfFormatado(cpf:any){const n=String(cpf||'').replace(/\D/g,'');return n.length===11?n.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/,'$1.$2.$3–$4'):(cpf||'–');}
  telefoneFormatado(telefone:any){const n=String(telefone||'').replace(/\D/g,'');return n.length===11?n.replace(/(\d{2})(\d{5})(\d{4})/,'($1) $2–$3'):telefone||'';}
  cepFormatado(cep:any){const n=String(cep||'').replace(/\D/g,'');return n.length===8?n.replace(/(\d{5})(\d{3})/,'$1–$2'):cep||'';}
  carregar(){this.api.get<any[]>('/admin/'+this.recurso).subscribe({next:r=>{this.rows=r;this.erro='';},error:e=>this.erro=e.error?.erro||'Falha ao consultar registros.'});}
  refs(){for(const r of ['clientes','agencias','tipos-conta'])this.api.get<any[]>('/admin/'+r).subscribe({next:v=>this.referencias[r]=v,error:()=>{}});}
  novo(){this.form={ativo:true,ativa:true,perfil:'CLIENTE',clienteId:null,agenciaId:null,tipoContaId:null};this.erroModal='';this.editando=true;}
  editar(r:any){this.form={...r,id:r.id,senha:''};if(this.recurso==='clientes')this.form={...r,...(r.endereco||{}),id:r.id,cpf:this.cpfFormatado(r.cpf),telefone:this.telefoneFormatado(r.telefone),cep:this.cepFormatado(r.endereco?.cep)};this.erroModal='';this.editando=true;}
  fechar(){this.editando=false;this.erroModal='';}
  salvar(){this.salvando=true;this.erroModal='';let payload={...this.form};const id=payload.id;delete payload.id;
  if(this.recurso==='clientes'){
    payload.cpf=String(payload.cpf||'').replace(/\D/g,'');
    if(payload.telefone)payload.telefone=String(payload.telefone).replace(/\D/g,'');
    const keys=['cep','logradouro','numero','complemento','bairro','cidade','uf'];const endereco:any={};
    for(const key of keys){endereco[key]=payload[key]||'';delete payload[key];}
    if(endereco.cep)payload.endereco=endereco;else delete payload.endereco;
  }
  if(this.recurso==='usuarios'&&payload.perfil==='ADMIN')payload.clienteId=null;
  const path='/admin/'+this.recurso+(id?'/'+id:'');
    const call=id?this.api.put(path,payload):this.api.post(path,payload);
    call.subscribe({next:()=>{this.salvando=false;this.sucesso='Registro salvo com sucesso.';this.fechar();this.carregar();this.refs();},error:e=>{this.salvando=false;this.erroModal=e.error?.erro||'Erro ao salvar.';if(e.error?.detalhes)this.erroModal+=' '+e.error.detalhes.join('; ');}});
  }
  alterarStatus(row:any){const status=prompt('Novo status: ATIVA, BLOQUEADA ou ENCERRADA',row.status);if(!status)return;this.api.patch('/admin/contas/'+row.id+'/status',{status:status.toUpperCase()}).subscribe({next:()=>{this.carregar();this.sucesso='Status atualizado.'},error:e=>this.erro=e.error?.erro||'Falha na atualização.'});}
  bloquear(row:any){if(row.status==='BLOQUEADA'||!confirm('Bloquear a conta '+row.numero+'?'))return;this.api.patch('/admin/contas/'+row.id+'/status',{status:'BLOQUEADA'}).subscribe({next:()=>{this.carregar();this.sucesso='Conta bloqueada.'},error:e=>this.erro=e.error?.erro||'Falha na atualização.'});}
  inativar(row:any){if(!confirm('Inativar '+row.nome+'?'))return;this.api.delete('/admin/clientes/'+row.id).subscribe({next:()=>{this.carregar();this.sucesso='Cliente inativado.';},error:e=>this.erro=e.error?.erro||'Falha ao inativar.'});}
}
