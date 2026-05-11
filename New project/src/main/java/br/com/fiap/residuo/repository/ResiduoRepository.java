package br.com.fiap.residuo.repository;

import br.com.fiap.residuo.entity.Residuo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResiduoRepository extends JpaRepository<Residuo, Long> {

    List<Residuo> findByIdPontoColeta(Long idPontoColeta);

    List<Residuo> findByTipoResiduoIgnoreCase(String tipoResiduo);

    List<Residuo> findByStatusIgnoreCase(String status);
}
