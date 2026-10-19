import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { LoginComponent } from './features/login.component';
import { AdminDashboardComponent } from './features/admin-dashboard.component';
import { AdminCrudComponent } from './features/admin-crud.component';
import { ClienteDashboardComponent } from './features/cliente-dashboard.component';
import { OperacaoComponent } from './features/operacao.component';
import { ExtratoComponent } from './features/extrato.component';
import { PerfilComponent } from './features/perfil.component';
export const routes:Routes=[
  {path:'login',component:LoginComponent},
{path:'admin',component:AdminDashboardComponent,canActivate:[authGuard],data:{perfil:'ADMIN'}},
...['clientes','agencias','tipos-conta','contas','usuarios'].map(r=>({path:'admin/'+r,component:AdminCrudComponent,canActivate:[authGuard],data:{perfil:'ADMIN',recurso:r}})),
{path:'cliente',component:ClienteDashboardComponent,canActivate:[authGuard],data:{perfil:'CLIENTE'}},
...['deposito','saque','transferencia'].map(r=>({path:'cliente/'+r,component:OperacaoComponent,canActivate:[authGuard],data:{perfil:'CLIENTE',tipo:r}})),
{path:'cliente/extrato',component:ExtratoComponent,canActivate:[authGuard],data:{perfil:'CLIENTE'}},
{path:'cliente/perfil',component:PerfilComponent,canActivate:[authGuard],data:{perfil:'CLIENTE'}},
{path:'',pathMatch:'full',redirectTo:'login'},
{path:'**',redirectTo:'login'}
];
