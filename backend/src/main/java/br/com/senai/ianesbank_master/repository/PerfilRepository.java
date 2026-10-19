package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Perfil;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PerfilRepository extends JpaRepository<Perfil,Long> {
    Optional<Perfil> findByCodigo(String codigo);
}
