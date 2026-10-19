package br.com.senai.ianesbank_master.controller;

import br.com.senai.ianesbank_master.dto.LoginForm;
import br.com.senai.ianesbank_master.service.AuthService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService auth;
 public AuthController(AuthService auth){this.auth=auth;}
 @GetMapping("/status") public Map<String,Object> status(){return Map.of("aplicacao","IaneSBank Master","status","UP");}
 @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody LoginForm form){return auth.login(form.login(),form.senha());}
}
