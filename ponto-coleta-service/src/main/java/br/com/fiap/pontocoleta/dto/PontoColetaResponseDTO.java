package br.com.fiap.pontocoleta.dto;

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
public class PontoColetaResponseDTO {

    private Long id;
    private String nome;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private Double latitude;
    private Double longitude;
    private Double capacidadeMaxima;
    private Double capacidadeAtual;
    private String tipoResiduoAceito;
    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
