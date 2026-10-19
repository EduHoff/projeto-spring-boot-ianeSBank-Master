package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    Optional<Cliente> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
