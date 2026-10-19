import { Component,inject,OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';
import { IconComponent } from '../shared/icon.component';
@Component({standalone:true,imports:[FormsModule,IconComponent],template:`
  <div class="client-page-title"><div class="eyebrow">MINHAS FINANÇAS</div><h1>Extrato</h1><p class="muted">Consulte os lançamentos por conta e período.</p></div>
  <div class="statement-filters"><div><label>Conta</label><select [(ngModel)]="contaId" (ngModelChange)="carregar()"><option [ngValue]="null">Selecione</option>@for(c of contas;track c.id){<option [ngValue]="c.id">{{c.numero}} · {{c.tipoConta}}</option>}</select></div><div><label>De</label><input type="date" [(ngModel)]="inicio"></div><div><label>Até</label><input type="date" [(ngModel)]="fim"></div><button class="btn primary" (click)="carregar()"><app-icon name="filter"/> Filtrar</button></div>
  @if(erro){<div class="alert error">{{erro}}</div>}
  <div class="statement-summary"><div><small>Entradas no período</small><strong class="money-in">+ {{moeda(entradas)}}</strong></div><div><small>Saídas no período</small><strong class="money-out">− {{moeda(saidas)}}</strong></div><div class="current-balance"><small>Saldo atual</small><strong>{{moeda(contaSelecionada?.saldo)}}</strong></div></div>
  <div class="statement-panel"><div class="statement-head"><h2>Lançamentos</h2><div class="statement-tabs"><button [class.active]="aba==='todos'" (click)="aba='todos'">Todos</button><button [class.active]="aba==='entradas'" (click)="aba='entradas'">Entradas</button><button [class.active]="aba==='saidas'" (click)="aba='saidas'">Saídas</button></div></div><div class="table-wrap"><table class="statement-table"><thead><tr><th>Data</th><th>Descrição</th><th>Operação</th><th>Valor</th><th>Saldo após</th></tr></thead><tbody>
  @for(m of filtrados;track m.id){<tr><td><b>{{dia(m.dataHora)}}</b><small>{{hora(m.dataHora)}}</small></td><td><b>{{m.descricao||descricaoPadrao(m)}}</b><small>{{m.codigo}}</small></td><td><span class="operation-badge" [class.deposit]="m.operacao==='DEPOSITO'" [class.withdraw]="m.operacao==='SAQUE'">{{rotuloOperacao(m)}}</span></td><td [class.money-out]="m.tipo==='DEBITO'" [class.money-in]="m.tipo==='CREDITO'"><b>{{m.tipo==='DEBITO'?'−':'+'}} {{moeda(m.valor)}}</b></td><td>{{moeda(m.saldoPosterior)}}</td></tr>}
  @empty{<tr><td colspan="5" class="empty">Nenhuma movimentação encontrada para os filtros.</td></tr>}</tbody></table></div></div>
  `})
export class ExtratoComponent implements OnInit {api=inject(ApiService);contas:any[]=[];movimentos:any[]=[];contaId:number|null=null;inicio='';fim='';erro='';aba:'todos'|'entradas'|'saidas'='todos';
  ngOnInit(){this.api.get<any[]>('/me/contas').subscribe({next:r=>{this.contas=r;this.contaId=r[0]?.id||null;this.carregar();},error:e=>this.erro=e.error?.erro||'Falha ao consultar contas.'});}
  get contaSelecionada(){return this.contas.find(c=>c.id===this.contaId);}
  get entradas(){return this.movimentos.filter(m=>m.tipo==='CREDITO').reduce((s,m)=>s+Number(m.valor),0);}
  get saidas(){return this.movimentos.filter(m=>m.tipo==='DEBITO').reduce((s,m)=>s+Number(m.valor),0);}
  get filtrados(){return this.movimentos.filter(m=>this.aba==='todos'||(this.aba==='entradas'?m.tipo==='CREDITO':m.tipo==='DEBITO'));}
  carregar(){if(!this.contaId){this.movimentos=[];return;}const p=new URLSearchParams();if(this.inicio)p.set('inicio',this.inicio);if(this.fim)p.set('fim',this.fim);this.api.get<any[]>('/me/contas/'+this.contaId+'/extrato'+(p.toString()?'?'+p.toString():'')).subscribe({next:r=>{this.movimentos=r;this.erro='';this.atualizarConta();},error:e=>this.erro=e.error?.erro||'Falha ao carregar extrato.'});}
  atualizarConta(){this.api.get<any[]>('/me/contas').subscribe({next:r=>this.contas=r,error:()=>{}});}
  moeda(v:any){return Number(v||0).toLocaleString('pt-BR',{style:'currency',currency:'BRL'});}dia(v:string){return v?new Date(v).toLocaleDateString('pt-BR'):'';}hora(v:string){return v?new Date(v).toLocaleTimeString('pt-BR',{hour:'2-digit',minute:'2-digit',second:'2-digit'}):'';}
  descricaoPadrao(m:any){return m.operacao==='DEPOSITO'?'Depósito':m.operacao==='SAQUE'?'Saque':'Transferência';}
  rotuloOperacao(m:any){return m.operacao==='TRANSFERENCIA'?'Transferência':m.operacao==='DEPOSITO'?'Depósito':'Saque';}
}
