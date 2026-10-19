package br.com.senai.ianesbank_master.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ApiException.class)
 ResponseEntity<Map<String,Object>> api(ApiException e){return ResponseEntity.status(e.getStatus()).body(Map.of("erro",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<Map<String,Object>> valid(MethodArgumentNotValidException e){
   var erros=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).toList();
   return ResponseEntity.badRequest().body(Map.of("erro","Dados inválidos","detalhes",erros));
 }
 @ExceptionHandler(DataIntegrityViolationException.class)
 ResponseEntity<Map<String,Object>> integrity(DataIntegrityViolationException e){return ResponseEntity.status(409).body(Map.of("erro","Operação não permitida: registro referenciado ou valor duplicado."));}
 @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
 ResponseEntity<Map<String,Object>> optimistic(ObjectOptimisticLockingFailureException e){return ResponseEntity.status(409).body(Map.of("erro","Conflito de atualização simultânea. Consulte o saldo e tente novamente."));}
 @ExceptionHandler(Exception.class)
 ResponseEntity<Map<String,Object>> unexpected(Exception e){return ResponseEntity.status(500).body(Map.of("erro","Erro interno inesperado. Consulte o terminal do servidor."));}
}
