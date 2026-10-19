import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth.service';
import { IconComponent } from './shared/icon.component';
@Component({selector:'app-root',standalone:true,imports:[RouterOutlet,RouterLink,RouterLinkActive,IconComponent],template:`
  @if(auth.usuario();as user){
    <div class="app-layout">
    @if(user.perfil==='ADMIN'){
      <aside class="sidebar">
      <a class="brand" routerLink="/admin"><span class="brand-icon">i</span><span>ianeSBank<small>MASTER</small></span></a>
      <div class="nav-category">GESTÃO BANCÁRIA</div>
      <nav>
      <a routerLink="/admin" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}"><app-icon name="dashboard"/> <span>Painel geral</span></a>
      <a routerLink="/admin/clientes" routerLinkActive="active"><app-icon name="users"/> <span>Clientes</span></a>
      <a routerLink="/admin/contas" routerLinkActive="active"><app-icon name="card"/> <span>Contas</span></a>
      <a routerLink="/admin/agencias" routerLinkActive="active"><app-icon name="bank"/> <span>Agências</span></a>
      <a routerLink="/admin/tipos-conta" routerLinkActive="active"><app-icon name="list"/> <span>Tipos de conta</span></a>
      <a routerLink="/admin/usuarios" routerLinkActive="active"><app-icon name="shield"/> <span>Usuários</span></a>
      </nav>
      <div class="sidebar-bottom"><span class="demo-dot"></span><span>Todos os sistemas<br>operando</span></div>
      </aside>
    }
    @if(user.perfil==='CLIENTE'){
      <aside class="sidebar client-sidebar">
      <a class="brand" routerLink="/cliente"><span class="brand-icon">i</span><span>ianeSBank<small>MASTER</small></span></a>
      <div class="nav-category">MINHA CONTA</div>
      <nav>
      <a routerLink="/cliente" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}"><app-icon name="dashboard"/> <span>Visão geral</span></a>
      <a routerLink="/cliente/deposito" routerLinkActive="active"><app-icon name="download"/> <span>Depósito</span></a>
      <a routerLink="/cliente/saque" routerLinkActive="active"><app-icon name="upload"/> <span>Saque</span></a>
      <a routerLink="/cliente/transferencia" routerLinkActive="active"><app-icon name="transfer"/> <span>Transferência</span></a>
      <a routerLink="/cliente/extrato" routerLinkActive="active"><app-icon name="list"/> <span>Extrato</span></a>
      <a routerLink="/cliente/perfil" routerLinkActive="active"><app-icon name="user"/> <span>Meu perfil</span></a>
      </nav>
      <div class="sidebar-bottom"><span class="demo-dot"></span><span>Ambiente seguro<br>e protegido</span></div>
      </aside>
    }
    <main class="workspace" [class.client-workspace]="user.perfil==='CLIENTE'">
    <header class="topbar">
    <div class="breadcrumbs"><span>{{user.perfil==='ADMIN'?'Administração':'Minha conta'}}</span>@if(subsecao()){<b>›</b><span>{{subsecao()}}</span>}<b>›</b><strong>{{paginaAtual()}}</strong></div>
    <div class="top-user"><button class="notification" type="button" aria-label="Notificações"><app-icon name="bell"/></button><span class="avatar">{{user.login.substring(0,1).toUpperCase()}}</span><span class="user-copy"><b>{{user.login}}</b><small>{{user.perfil==='ADMIN'?'Administrador':'Cliente'}}</small></span><button class="logout-button" (click)="auth.sair()"><app-icon name="logout"/> Sair</button></div>
    </header>
    <div class="page"><router-outlet /></div>
    </main>
    </div>
  } @else {<router-outlet />}
  `})
export class AppComponent {
  auth=inject(AuthService);router=inject(Router);
  paginaAtual(){const p=this.router.url.split('?')[0];const nomes:Record<string,string>={'/admin':'Painel geral','/admin/clientes':'Clientes','/admin/contas':'Contas','/admin/agencias':'Agências','/admin/tipos-conta':'Tipos de conta','/admin/usuarios':'Usuários','/cliente':'Visão geral','/cliente/deposito':'Depósito','/cliente/saque':'Saque','/cliente/transferencia':'Transferência','/cliente/extrato':'Extrato','/cliente/perfil':'Meu perfil'};return nomes[p]||'Painel geral';}
  subsecao(){return this.router.url.startsWith('/admin/usuarios')?'Segurança':(this.router.url.startsWith('/admin/')?'Cadastros':'');}
}
