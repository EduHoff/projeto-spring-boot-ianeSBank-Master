import { HttpInterceptorFn } from '@angular/common/http';
export const authInterceptor: HttpInterceptorFn = (req,next) => {
  const token=sessionStorage.getItem('sb_token');
  return next(token && req.url.startsWith('http://localhost:8080/api/')?req.clone({setHeaders:{Authorization:`Bearer ${token}`}}):req);
};
