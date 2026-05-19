package br.com.fiap.pontocoleta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class PontoColetaRequestDTO {

    @NotBlank(message = "O nome do ponto de coleta e obrigatorio")
    private String nome;

    @NotBlank(message = "O endereco e obrigatorio")
    private String endereco;

    @NotBlank(message = "A cidade e obrigatoria")
    private String cidade;

    @NotBlank(message = "O estado e obrigatorio")
    @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres")
    private String estado;

    @NotBlank(message = "O CEP e obrigatorio")
    private String cep;

    private Double latitude;

    private Double longitude;

    @NotNull(message = "A capacidade maxima e obrigatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "A capacidade maxima deve ser maior que zero")
    private Double capacidadeMaxima;

    @NotNull(message = "A capacidade atual e obrigatoria")
    @DecimalMin(value = "0.0", message = "A capacidade atual nao pode ser negativa")
    private Double capacidadeAtual;

    @NotBlank(message = "O tipo de residuo aceito e obrigatorio")
    private String tipoResiduoAceito;

    @Pattern(regexp = "^(ATIVO|INATIVO|LOTADO)$", message = "O status deve ser ATIVO, INATIVO ou LOTADO")
    private String status;
}
