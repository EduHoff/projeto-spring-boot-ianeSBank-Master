import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../core/api.service';
import { IconComponent } from '../shared/icon.component';

@Component({standalone:true,imports:[FormsModule,IconComponent],template:`
  <div class="client-page-title"><div class="eyebrow">MOVIMENTAÇÕES</div><h1>{{titulo}}</h1><p class="muted">{{subtitulo}}</p></div>
  <div class="operation-card"><h2>Dados {{tipo==='transferencia'?'da transferência':tipo==='deposito'?'do depósito':'do saque'}}</h2>
  <form (ngSubmit)="preparar()" #formOperacao="ngForm">
  <label>{{tipo==='deposito'?'Conta de destino':'Conta de origem'}}</label>
  <select [(ngModel)]="contaId" name="contaId" required><option [ngValue]="null">Selecione uma conta</option>@for(c of contas;track c.id){<option [ngValue]="c.id">{{c.numero}} · {{c.tipoConta}}{{tipo!=='saque'?' · Saldo '+money(c.saldo):''}}</option>}</select>
  @if(tipo==='saque'&&contaSelecionada){<div class="available-balance"><span>Saldo disponível</span><b>{{money(contaSelecionada.saldo)}}</b></div>}
  @if(tipo==='transferencia'){<label>Conta de destino</label><input name="numeroDestino" [(ngModel)]="numeroDestino" (ngModelChange)="destino=null" placeholder="Número da conta (6 dígitos)" required>}
  <label>Valor</label><div class="currency-input"><span>R$</span><input type="number" name="valor" [(ngModel)]="valor" min="0.01" step="0.01" placeholder="0,00" required></div>
  @if(tipo!=='transferencia'){<div class="quick-values">@for(v of [50,100,500,1000];track v){<button type="button" (click)="valor=v">{{money(v).replace(',00','')}}</button>}</div>}
  <label>Descrição <span class="muted-label">(opcional)</span></label><textarea rows="3" name="descricao" [(ngModel)]="descricao" [placeholder]="placeholderDescricao"></textarea>
  @if(erro){<div class="alert error">{{erro}}</div>}@if(sucesso){<div class="alert success">{{sucesso}}</div>}
  <button class="btn primary full operation-submit" [disabled]="enviando||formOperacao.invalid||!valor||valor<=0">{{enviando?'Processando...':'Confirmar '+titulo.toLowerCase()}} <app-icon name="chevron"/></button>
  </form></div>

  @if(confirmando&&destino){<div class="modal-backdrop" (click)="cancelarConfirmacao()"><div class="transfer-confirm" (click)="$event.stopPropagation()">
    <div class="confirm-icon"><app-icon name="transfer"/></div><div class="eyebrow">CONFIRME OS DADOS</div><h2>Confirmar transferência?</h2>
    <p class="confirm-sentence">Transferência para <b>{{destino.titular}}</b> na conta <b>{{destino.numero}}</b>.</p>
    <div class="confirm-details"><span><small>Valor</small><b>{{money(valor)}}</b></span><span><small>Conta de origem</small><b>{{contaSelecionada?.numero}}</b></span><span><small>Instituição</small><b>ianeSBank Master</b></span></div>
    <div class="confirm-warning"><app-icon name="shield"/><span>Confira os dados. Após a confirmação, a transferência será registrada nas duas contas.</span></div>
    <div class="modal-actions"><button type="button" class="btn secondary" (click)="cancelarConfirmacao()">Voltar</button><button type="button" class="btn primary" (click)="executar()" [disabled]="enviando">{{enviando?'Transferindo...':'Confirmar transferência'}}</button></div>
    </div></div>}
    `})
export class OperacaoComponent implements OnInit {
  api=inject(ApiService);route=inject(ActivatedRoute);tipo='deposito';contas:any[]=[];contaId:number|null=null;numeroDestino='';valor:number|null=null;descricao='';erro='';sucesso='';enviando=false;confirmando=false;destino:any=null;
  ngOnInit(){this.route.data.subscribe(d=>{this.tipo=d['tipo']||'deposito';this.erro='';this.sucesso='';this.confirmando=false;this.destino=null;});this.recarregar();}
  get titulo(){return this.tipo==='deposito'?'Depósito':this.tipo==='saque'?'Saque':'Transferência';}
  get subtitulo(){return this.tipo==='deposito'?'Adicione saldo à sua conta.':this.tipo==='saque'?'Retire valores da sua conta.':'Envie dinheiro para outra conta ianeSBank Master.';}
  get placeholderDescricao(){return this.tipo==='deposito'?'Ex.: Reserva do mês':this.tipo==='saque'?'Ex.: Retirada para despesas':'Ex.: Aluguel de outubro';}
  get contaSelecionada(){return this.contas.find(c=>c.id===this.contaId);}
  money(v:any){return Number(v||0).toLocaleString('pt-BR',{style:'currency',currency:'BRL'});}
  recarregar(){this.api.get<any[]>('/me/contas').subscribe({next:r=>{this.contas=r;this.contaId=this.contaId||r.find(x=>x.status==='ATIVA')?.id||null;},error:e=>this.erro=e.error?.erro||'Não foi possível carregar as contas.'});}
  preparar(){this.erro='';this.sucesso='';if(this.tipo!=='transferencia'){this.executar();return;}this.enviando=true;this.api.get('/me/contas/destino/'+encodeURIComponent(this.numeroDestino.trim())).subscribe({next:r=>{this.enviando=false;this.destino=r;this.confirmando=true;},error:e=>{this.enviando=false;this.erro=e.error?.erro||'Não foi possível localizar a conta de destino.';}});}
  cancelarConfirmacao(){if(this.enviando)return;this.confirmando=false;}
  executar(){this.erro='';this.sucesso='';this.enviando=true;let path='',body:any={};
  if(this.tipo==='transferencia'){path='/me/transferencias';body={contaOrigemId:this.contaId,numeroDestino:this.numeroDestino.trim(),valor:this.valor,descricao:this.descricao};}
  else{path=this.tipo==='deposito'?'/me/depositos':'/me/saques';body={contaId:this.contaId,valor:this.valor,descricao:this.descricao};}
  this.api.post(path,body).subscribe({next:r=>{this.enviando=false;this.confirmando=false;this.sucesso='Operação concluída. Comprovante: '+r.codigo;this.valor=null;this.descricao='';this.numeroDestino='';this.destino=null;this.recarregar();},error:e=>{this.enviando=false;this.confirmando=false;this.erro=e.error?.erro||'Não foi possível concluir a operação.';}});
  }
}
