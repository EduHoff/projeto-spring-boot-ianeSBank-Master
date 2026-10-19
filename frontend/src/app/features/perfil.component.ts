import { Component, inject,OnInit } from '@angular/core';
import { ApiService } from '../core/api.service';
import { IconComponent } from '../shared/icon.component';
@Component({standalone:true,imports:[IconComponent],template:`
  <div class="client-page-title"><div class="eyebrow">MEUS DADOS</div><h1>Meu perfil</h1><p class="muted">Dados cadastrais vinculados ao seu acesso.</p></div>
  @if(erro){<div class="alert error">{{erro}}</div>}
  <div class="profile-hero"><span>{{inicial}}</span><div><h2>{{nomeCurto}}</h2><small>Cliente</small><em><i></i>Cadastro ativo</em></div></div>
  <div class="profile-card"><h2><app-icon name="user"/> Dados pessoais</h2><div class="detail-row"><span>Nome completo</span><b>{{d.cliente?.nome||'–'}}</b></div><div class="detail-row"><span>CPF</span><b>{{cpf(d.cliente?.cpf)}}</b></div><div class="detail-row"><span>E-mail</span><b>{{d.cliente?.email||'–'}}</b></div><div class="detail-row"><span>Telefone</span><b>{{telefone(d.cliente?.telefone)}}</b></div></div>
  <div class="profile-card"><h2><app-icon name="lock"/> Acesso</h2><div class="detail-row"><span>Usuário</span><b>{{d.usuario?.login||'–'}}</b></div><div class="detail-row"><span>Perfil</span><b class="profile-type">Cliente</b></div></div>
  `})
export class PerfilComponent implements OnInit {api=inject(ApiService);d:any={};erro='';ngOnInit(){this.api.get('/me/perfil').subscribe({next:r=>this.d=r,error:e=>this.erro=e.error?.erro||'Falha ao consultar perfil.'});}get inicial(){return String(this.d.cliente?.nome||'?').charAt(0).toUpperCase();}get nomeCurto(){const p=String(this.d.cliente?.nome||'Cliente').split(/\s+/);return p.length>1?p[0]+' '+p.at(-1)?.charAt(0)+'.':p[0];}cpf(v:any){const n=String(v||'').replace(/\D/g,'');return n.length===11?n.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/,'$1.$2.$3–$4'):(v||'–');}telefone(v:any){const n=String(v||'').replace(/\D/g,'');return n.length===11?n.replace(/(\d{2})(\d{5})(\d{4})/,'($1) $2–$3'):(v||'–');}}
