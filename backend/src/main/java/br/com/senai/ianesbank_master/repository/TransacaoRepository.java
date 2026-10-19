package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Transacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransacaoRepository extends JpaRepository<Transacao,Long> {
    List<Transacao> findByContaOrigem_IdContaOrContaDestino_IdContaOrderByDataHoraDesc(Long a, Long b);
}
