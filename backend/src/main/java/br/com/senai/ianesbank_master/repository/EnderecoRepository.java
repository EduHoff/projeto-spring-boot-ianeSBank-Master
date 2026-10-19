package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Endereco;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {
    Optional<Endereco> findByCliente_IdCliente(Long id);
}
