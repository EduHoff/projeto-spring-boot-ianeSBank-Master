package br.com.senai.ianesbank_master.controller;

import br.com.senai.ianesbank_master.dto.*;
import br.com.senai.ianesbank_master.service.AdminService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin")
public class AdminController {
 private final AdminService s;public AdminController(AdminService s){this.s=s;}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(){return s.dashboard();}
 @GetMapping("/clientes") public List<Map<String,Object>> clientes(){return s.listarClientes();}
 @GetMapping("/clientes/{id}") public Map<String,Object> cliente(@PathVariable Long id){return s.consultarCliente(id);}
 @PostMapping("/clientes") public Map<String,Object> criarCliente(@Valid @RequestBody ClienteForm f){return s.salvarCliente(null,f);}
 @PutMapping("/clientes/{id}") public Map<String,Object> alterarCliente(@PathVariable Long id,@Valid @RequestBody ClienteForm f){return s.salvarCliente(id,f);}
 @DeleteMapping("/clientes/{id}") public void inativarCliente(@PathVariable Long id){s.inativarCliente(id);}
 @GetMapping("/agencias") public List<Map<String,Object>> agencias(){return s.listarAgencias();}
 @PostMapping("/agencias") public Map<String,Object> criarAgencia(@Valid @RequestBody AgenciaForm f){return s.salvarAgencia(null,f);}
 @PutMapping("/agencias/{id}") public Map<String,Object> alterarAgencia(@PathVariable Long id,@Valid @RequestBody AgenciaForm f){return s.salvarAgencia(id,f);}
 @GetMapping("/tipos-conta") public List<Map<String,Object>> tipos(){return s.listarTipos();}
 @PostMapping("/tipos-conta") public Map<String,Object> criarTipo(@Valid @RequestBody TipoContaForm f){return s.salvarTipo(null,f);}
 @PutMapping("/tipos-conta/{id}") public Map<String,Object> alterarTipo(@PathVariable Long id,@Valid @RequestBody TipoContaForm f){return s.salvarTipo(id,f);}
 @GetMapping("/contas") public List<Map<String,Object>> contas(){return s.listarContas();}
 @PostMapping("/contas") public Map<String,Object> abrir(@Valid @RequestBody ContaForm f){return s.abrirConta(f);}
 @PatchMapping("/contas/{id}/status") public Map<String,Object> status(@PathVariable Long id,@Valid @RequestBody ContaStatusForm f){return s.alterarStatus(id,f);}
 @GetMapping("/usuarios") public List<Map<String,Object>> usuarios(){return s.listarUsuarios();}
 @PostMapping("/usuarios") public Map<String,Object> criarUsuario(@Valid @RequestBody UsuarioForm f){return s.salvarUsuario(null,f);}
 @PutMapping("/usuarios/{id}") public Map<String,Object> alterarUsuario(@PathVariable Long id,@Valid @RequestBody UsuarioForm f){return s.salvarUsuario(id,f);}
}
