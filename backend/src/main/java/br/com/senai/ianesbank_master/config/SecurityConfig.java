package br.com.senai.ianesbank_master.config;

import br.com.senai.ianesbank_master.security.JwtAuthFilter;
import br.com.senai.ianesbank_master.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService userDetailsService(UsuarioRepository usuarios){
   return login->usuarios.findByLogin(login)
     .map(u->User.withUsername(u.getLogin()).password(u.getSenhaHash()).roles(u.getPerfil().getCodigo()).disabled(!Boolean.TRUE.equals(u.getAtivo())).build())
     .orElseThrow(()->new UsernameNotFoundException("Usuário não encontrado."));
 }
 @Bean CorsConfigurationSource corsConfigurationSource(){
   var c=new CorsConfiguration();c.setAllowedOriginPatterns(List.of("http://localhost:*","http://127.0.0.1:*"));
   c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("Authorization","Content-Type"));
   c.setMaxAge(3600L);
   var src=new UrlBasedCorsConfigurationSource();src.registerCorsConfiguration("/**",c);return src;
 }
 @Bean SecurityFilterChain chain(HttpSecurity http,JwtAuthFilter jwt,CorsConfigurationSource corsConfigurationSource) throws Exception {
   return http.csrf(AbstractHttpConfigurer::disable).cors(c->c.configurationSource(corsConfigurationSource))
     .httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).logout(AbstractHttpConfigurer::disable)
     .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     .authorizeHttpRequests(a->a.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll().requestMatchers("/api/auth/login","/api/auth/status").permitAll()
       .requestMatchers("/api/admin/**").hasRole("ADMIN").requestMatchers("/api/me/**").hasRole("CLIENTE").anyRequest().authenticated())
     .addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();
 }
}
