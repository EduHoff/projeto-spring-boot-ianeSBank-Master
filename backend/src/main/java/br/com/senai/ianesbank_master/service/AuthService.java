package br.com.senai.ianesbank_master.service;

import br.com.senai.ianesbank_master.repository.UsuarioRepository;
import br.com.senai.ianesbank_master.entity.Usuario;
import br.com.senai.ianesbank_master.exception.ApiException;
import br.com.senai.ianesbank_master.security.TokenService;
import br.com.senai.ianesbank_master.util.Views;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthService {
 private final UsuarioRepository usuarios; private final PasswordEncoder encoder;private final TokenService tokens;
 public AuthService(UsuarioRepository u,PasswordEncoder e,TokenService t){this.usuarios=u;this.encoder=e;this.tokens=t;}
 @Transactional
 public Map<String,Object> login(String login,String senha){
   Usuario u=usuarios.findByLogin(login.trim()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Login ou senha inválidos."));
   if(!Boolean.TRUE.equals(u.getAtivo())||(u.getCliente()!=null&&!Boolean.TRUE.equals(u.getCliente().getAtivo()))||!encoder.matches(senha,u.getSenhaHash()))throw new ApiException(HttpStatus.UNAUTHORIZED,"Login ou senha inválidos.");
   u.setUltimoAcesso(LocalDateTime.now());
   return Views.m("token",tokens.gerar(u.getLogin()),"usuario",Views.usuario(u));
 }
}
