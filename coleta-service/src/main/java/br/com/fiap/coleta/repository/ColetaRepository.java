package br.com.fiap.coleta.repository;

import br.com.fiap.coleta.entity.Coleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColetaRepository extends JpaRepository<Coleta, Long> {

    List<Coleta> findByIdPontoColeta(Long idPontoColeta);

    List<Coleta> findByIdResiduo(Long idResiduo);

    List<Coleta> findByStatusIgnoreCase(String status);

    Long countByStatusIgnoreCase(String status);

    @Query("SELECT COALESCE(SUM(c.quantidadeColetada), 0) FROM Coleta c WHERE c.status = 'REALIZADA'")
    Double somarQuantidadeColetadaRealizada();
}
