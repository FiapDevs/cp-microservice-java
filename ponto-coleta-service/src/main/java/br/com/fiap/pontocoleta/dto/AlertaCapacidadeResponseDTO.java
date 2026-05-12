package br.com.fiap.pontocoleta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaCapacidadeResponseDTO {

    private Long id;
    private String nome;
    private String cidade;
    private String estado;
    private Double capacidadeMaxima;
    private Double capacidadeAtual;
    private Double percentualOcupacao;
    private String status;
    private String mensagem;
}
