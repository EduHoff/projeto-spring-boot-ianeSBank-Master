package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.TipoConta;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TipoContaRepository extends JpaRepository<TipoConta,Long> {
    Optional<TipoConta> findByCodigo(String codigo);
}
