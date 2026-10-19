import { Component,inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../core/auth.service';
import { IconComponent } from '../shared/icon.component';
@Component({standalone:true,imports:[FormsModule,IconComponent],template:`
  <div class="login-layout">
  <section class="login-info">
  <div class="login-orbit orbit-one"></div><div class="login-orbit orbit-two"></div>
  <div class="login-logo"><span class="login-logo-mark">i</span><span>ianeSBank<small>MASTER</small></span></div>
  <div class="login-copy">
  <div class="login-heading">Seu dinheiro.<br><em>Sob seu controle.</em></div>
  <p>Conta digital, Pix, cartões e investimentos em um só lugar. Simples no dia a dia, sólido enquanto funcionar</p>
  <div class="bank-card">
  <div class="card-top"><b>ianeSBank</b><span>MASTER</span><i>)))</i></div>
  <div class="card-chip"></div>
  <div class="card-bottom"><span>•••• •••• •••• 2048</span><small>12/31</small></div>
  </div>
  </div>
  <div class="login-benefits"><span><app-icon name="shield"/> Proteção em cada transação</span><span><app-icon name="clock"/> Atendimento 24 horas</span><span><app-icon name="bolt"/> Pix na hora, todo dia</span></div>
  </section>

  <section class="login-box">
  <div class="login-top-note">Ainda não é cliente? <b>Abra sua conta</b></div>
  <div class="form-wrap">
  <div class="eyebrow">BEM-VINDO DE VOLTA</div><h1>Acesse sua conta</h1><p class="muted">Entre com seu usuário e senha para continuar.</p>
  <form (ngSubmit)="submit()" #formLogin="ngForm">
  <label>Usuário</label><input [(ngModel)]="login" name="login" autocomplete="username" placeholder="Digite seu usuário" required>
  <label>Senha</label><div class="password-field"><input [(ngModel)]="senha" name="senha" [type]="mostrarSenha?'text':'password'" autocomplete="current-password" placeholder="Digite sua senha" required><button type="button" (click)="mostrarSenha=!mostrarSenha" [attr.aria-label]="mostrarSenha?'Ocultar senha':'Mostrar senha'"><svg viewBox="0 0 24 24" aria-hidden="true"><path d="M2.8 12s3.3-5 9.2-5 9.2 5 9.2 5-3.3 5-9.2 5-9.2-5-9.2-5Z"></path><circle cx="12" cy="12" r="2.4"></circle></svg></button></div>
  <div class="login-options"><label><input type="checkbox" [(ngModel)]="lembrar" name="lembrar"> Lembrar meu usuário</label><button type="button">Esqueci minha senha</button></div>
  @if(erro){<div class="login-error">{{erro}}</div>}
  <button class="btn primary full login-submit" [disabled]="ocupado||formLogin.invalid">{{ocupado?'Acessando...':'Entrar'}} <app-icon name="chevron"/></button>
  </form>
  <div class="login-security"><span class="security-icon" aria-hidden="true"><app-icon name="lock"/></span><p>Ambiente protegido. O ianeSBank Master nunca pede sua<br>senha por telefone, e-mail ou mensagem.</p></div>
  </div>
  <div class="login-footer"><span>© 2026 ianeSBank Master</span><span>Privacidade</span><span>Segurança</span><span>Central de ajuda</span></div>
  </section>
  </div>
  `})
  export class LoginComponent {
    auth=inject(AuthService);login=localStorage.getItem('sb_login')||'';senha='';erro='';ocupado=false;mostrarSenha=false;lembrar=!!localStorage.getItem('sb_login');
    submit(){this.erro='';this.ocupado=true;if(this.lembrar)localStorage.setItem('sb_login',this.login);else localStorage.removeItem('sb_login');this.auth.entrar(this.login,this.senha).subscribe({next:r=>{this.ocupado=false;this.auth.salvar(r)},error:e=>{this.ocupado=false;this.erro=e.error?.erro||'Não foi possível autenticar. Confira o servidor.';}});}
  }
