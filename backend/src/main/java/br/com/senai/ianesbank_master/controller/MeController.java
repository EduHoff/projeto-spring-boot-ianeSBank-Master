package br.com.senai.ianesbank_master.controller;

import br.com.senai.ianesbank_master.dto.*;
import br.com.senai.ianesbank_master.service.BankService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/me")
public class MeController {
 private final BankService s;public MeController(BankService s){this.s=s;}
 @GetMapping("/perfil") public Map<String,Object> perfil(Authentication a){return s.meuPerfil(a.getName());}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(Authentication a){return s.meuDashboard(a.getName());}
 @GetMapping("/contas") public List<Map<String,Object>> contas(Authentication a){return s.minhasContas(a.getName());}
 @GetMapping("/contas/destino/{numero}") public Map<String,Object> contaDestino(@PathVariable String numero,Authentication a){return s.contaDestino(numero,a.getName());}
 @GetMapping("/contas/{id}/extrato") public List<Map<String,Object>> extrato(@PathVariable Long id,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate inicio,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fim,Authentication a){return s.extrato(id,inicio,fim,a.getName());}
 @PostMapping("/depositos") public Map<String,Object> deposito(@Valid @RequestBody OperacaoForm f,Authentication a){return s.deposito(f,a.getName());}
 @PostMapping("/saques") public Map<String,Object> saque(@Valid @RequestBody OperacaoForm f,Authentication a){return s.saque(f,a.getName());}
 @PostMapping("/transferencias") public Map<String,Object> transferencia(@Valid @RequestBody TransferenciaForm f,Authentication a){return s.transferencia(f,a.getName());}
}
