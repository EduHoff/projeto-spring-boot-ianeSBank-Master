import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
export const authGuard: CanActivateFn = (route) => {
  const a=inject(AuthService),r=inject(Router);
  if(!a.autenticado())return r.parseUrl('/login');
  const perfil=route.data['perfil'];
  return !perfil||perfil===a.usuario()?.perfil ? true:r.parseUrl(a.usuario()?.perfil==='ADMIN'?'/admin':'/cliente');
};
