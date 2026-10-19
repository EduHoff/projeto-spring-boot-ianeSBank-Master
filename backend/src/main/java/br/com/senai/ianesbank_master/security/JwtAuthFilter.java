package br.com.senai.ianesbank_master.security;

import br.com.senai.ianesbank_master.repository.UsuarioRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 private final TokenService tokens;
 private final UsuarioRepository usuarios;
 public JwtAuthFilter(TokenService tokens,UsuarioRepository usuarios){this.tokens=tokens;this.usuarios=usuarios;}
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
   String h=request.getHeader("Authorization");
   if(h!=null && h.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication()==null){
     try {
       String login=tokens.validar(h.substring(7));
       usuarios.findByLogin(login).filter(u->Boolean.TRUE.equals(u.getAtivo()) && (u.getCliente()==null||Boolean.TRUE.equals(u.getCliente().getAtivo()))).ifPresent(u->{
         var auth=new UsernamePasswordAuthenticationToken(u.getLogin(),null,java.util.List.of(new SimpleGrantedAuthority("ROLE_"+u.getPerfil().getCodigo())));
         SecurityContextHolder.getContext().setAuthentication(auth);
       });
     } catch (Exception ignored) { SecurityContextHolder.clearContext(); }
   }
   chain.doFilter(request,response);
 }
}
