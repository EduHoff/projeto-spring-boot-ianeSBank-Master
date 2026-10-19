package br.com.senai.ianesbank_master.repository;

import br.com.senai.ianesbank_master.entity.Movimentacao;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MovimentacaoRepository extends JpaRepository<Movimentacao,Long> {
    List<Movimentacao> findByConta_IdContaOrderByDataHoraDescIdMovimentacaoDesc(Long id);
    List<Movimentacao> findByConta_IdContaAndDataHoraBetweenOrderByDataHoraDescIdMovimentacaoDesc(Long id, LocalDateTime inicio, LocalDateTime fim);
}
