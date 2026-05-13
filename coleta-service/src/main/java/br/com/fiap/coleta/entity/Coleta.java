package br.com.fiap.coleta.entity;

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
@Table(name = "T_COLETA")
public class Coleta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coleta")
    @SequenceGenerator(name = "seq_coleta", sequenceName = "SEQ_COLETA", allocationSize = 1)
    @Column(name = "ID_COLETA")
    private Long id;

    @Column(name = "ID_PONTO_COLETA", nullable = false)
    private Long idPontoColeta;

    @Column(name = "ID_RESIDUO", nullable = false)
    private Long idResiduo;

    @Column(name = "DT_AGENDAMENTO", nullable = false)
    private LocalDateTime dataAgendamento;

    @Column(name = "DT_COLETA")
    private LocalDateTime dataColeta;

    @Column(name = "NR_QUANTIDADE_COLETADA", columnDefinition = "NUMBER(12,2)")
    private Double quantidadeColetada;

    @Column(name = "ST_COLETA", nullable = false, length = 20)
    private String status;

    @Column(name = "DS_OBSERVACAO", length = 250)
    private String observacao;

    @Column(name = "DT_CRIACAO", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "DT_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;
}
