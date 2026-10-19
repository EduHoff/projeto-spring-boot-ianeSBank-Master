package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Agencia;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AgenciaRepository extends JpaRepository<Agencia,Long> {
    Optional<Agencia> findByNumero(String numero);
}
