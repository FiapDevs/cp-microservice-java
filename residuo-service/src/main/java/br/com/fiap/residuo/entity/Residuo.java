package br.com.fiap.residuo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_RESIDUO")
public class Residuo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_residuo")
    @SequenceGenerator(name = "seq_residuo", sequenceName = "SEQ_RESIDUO", allocationSize = 1)
    @Column(name = "ID_RESIDUO")
    private Long id;

    @Column(name = "ID_PONTO_COLETA", nullable = false)
    private Long idPontoColeta;

    @Column(name = "NM_RESIDUO", nullable = false, length = 100)
    private String nome;

    @Column(name = "DS_TIPO_RESIDUO", nullable = false, length = 50)
    private String tipoResiduo;

    @Column(name = "DS_RESIDUO", length = 500)
    private String descricao;

    @Column(name = "NR_QUANTIDADE", nullable = false, columnDefinition = "NUMBER(12,2)")
    private Double quantidade;

    @Column(name = "DS_UNIDADE_MEDIDA", nullable = false, length = 20)
    private String unidadeMedida;

    @Column(name = "ST_RESIDUO", nullable = false, length = 20)
    private String status;

    @Column(name = "DT_REGISTRO", nullable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "DT_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;
}
