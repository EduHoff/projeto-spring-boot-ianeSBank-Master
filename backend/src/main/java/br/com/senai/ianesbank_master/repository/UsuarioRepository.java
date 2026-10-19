package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    @EntityGraph(attributePaths={"cliente","perfil"})
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByCliente_IdCliente(Long id);
    boolean existsByLogin(String login);
}
