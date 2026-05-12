package br.com.fiap.pontocoleta.repository;

import br.com.fiap.pontocoleta.entity.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    List<PontoColeta> findByCidadeIgnoreCase(String cidade);

    List<PontoColeta> findByStatusIgnoreCase(String status);

    List<PontoColeta> findByTipoResiduoAceitoIgnoreCase(String tipoResiduoAceito);

    @Query("""
            SELECT p
            FROM PontoColeta p
            WHERE (p.capacidadeAtual / p.capacidadeMaxima) * 100 >= :percentualMinimo
            """)
    List<PontoColeta> findComCapacidadeAcimaDoLimite(@Param("percentualMinimo") Double percentualMinimo);
}
