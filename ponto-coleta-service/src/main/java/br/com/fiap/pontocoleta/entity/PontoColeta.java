package br.com.fiap.pontocoleta.entity;

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
@Table(name = "T_PONTO_COLETA")
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ponto_coleta")
    @SequenceGenerator(name = "seq_ponto_coleta", sequenceName = "SEQ_PONTO_COLETA", allocationSize = 1)
    @Column(name = "ID_PONTO_COLETA")
    private Long id;

    @Column(name = "NM_PONTO_COLETA", nullable = false, length = 120)
    private String nome;

    @Column(name = "DS_ENDERECO", nullable = false, length = 200)
    private String endereco;

    @Column(name = "NM_CIDADE", nullable = false, length = 100)
    private String cidade;

    @Column(name = "SG_ESTADO", nullable = false, length = 2)
    private String estado;

    @Column(name = "NR_CEP", nullable = false, length = 10)
    private String cep;

    @Column(name = "NR_LATITUDE", columnDefinition = "NUMBER(10,6)")
    private Double latitude;

    @Column(name = "NR_LONGITUDE", columnDefinition = "NUMBER(10,6)")
    private Double longitude;

    @Column(name = "NR_CAPACIDADE_MAXIMA", nullable = false, columnDefinition = "NUMBER(12,2)")
    private Double capacidadeMaxima;

    @Column(name = "NR_CAPACIDADE_ATUAL", nullable = false, columnDefinition = "NUMBER(12,2)")
    private Double capacidadeAtual;

    @Column(name = "DS_TIPO_RESIDUO_ACEITO", nullable = false, length = 80)
    private String tipoResiduoAceito;

    @Column(name = "ST_PONTO_COLETA", nullable = false, length = 20)
    private String status;

    @Column(name = "DT_CRIACAO", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "DT_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;
}
