package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Conta;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface ContaRepository extends JpaRepository<Conta,Long> {
    Optional<Conta> findByNumero(String numero);
    List<Conta> findByCliente_IdClienteOrderByIdContaDesc(Long clienteId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Conta c where c.idConta = :id")
    Optional<Conta> bloquearParaOperacao(@Param("id") Long id);
}
