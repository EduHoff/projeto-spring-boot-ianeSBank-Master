import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from './api.service';
@Injectable({providedIn:'root'})
export class AuthService {
  usuario=signal<any>(this.read());
  constructor(private api:ApiService,private router:Router){}
  private read():any {try{return JSON.parse(sessionStorage.getItem('sb_user')||'null');}catch{return null;}}
  entrar(login:string,senha:string){return this.api.post('/auth/login',{login,senha});}
  salvar(result:any){sessionStorage.setItem('sb_token',result.token);sessionStorage.setItem('sb_user',JSON.stringify(result.usuario));this.usuario.set(result.usuario);this.router.navigateByUrl(result.usuario.perfil==='ADMIN'?'/admin':'/cliente');}
  sair(){sessionStorage.removeItem('sb_token');sessionStorage.removeItem('sb_user');this.usuario.set(null);this.router.navigateByUrl('/login');}
  autenticado(){return !!this.usuario() && !!sessionStorage.getItem('sb_token');}
}
