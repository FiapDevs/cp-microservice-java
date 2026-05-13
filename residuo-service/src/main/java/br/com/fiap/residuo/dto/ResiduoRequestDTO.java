package br.com.fiap.residuo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ResiduoRequestDTO(
        @NotNull(message = "O ID do ponto de coleta e obrigatorio")
        Long idPontoColeta,

        @NotBlank(message = "O nome do residuo e obrigatorio")
        String nome,

        @NotBlank(message = "O tipo do residuo e obrigatorio")
        String tipoResiduo,

        String descricao,

        @NotNull(message = "A quantidade e obrigatoria")
        @DecimalMin(value = "0.0", inclusive = true, message = "A quantidade nao pode ser negativa")
        Double quantidade,

        @Pattern(regexp = "\\S.*", message = "A unidade de medida nao pode estar em branco")
        String unidadeMedida,

        @Pattern(regexp = "(?i:ATIVO|INATIVO)", message = "O status deve ser ATIVO ou INATIVO")
        String status
) {
}
