package br.com.fiap.residuo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ResiduoRequestDTO(
        @NotNull(message = "O ID do ponto de coleta é obrigatório")
        Long idPontoColeta,

        @NotBlank(message = "O nome do resíduo é obrigatório")
        String nome,

        @NotBlank(message = "O tipo do resíduo é obrigatório")
        String tipoResiduo,

        String descricao,

        @NotNull(message = "A quantidade é obrigatória")
        @DecimalMin(value = "0.0", inclusive = true, message = "A quantidade não pode ser negativa")
        Double quantidade,

        @Pattern(regexp = "\\S.*", message = "A unidade de medida não pode estar em branco")
        String unidadeMedida,

        @Pattern(regexp = "(?i:ATIVO|INATIVO)", message = "O status deve ser ATIVO ou INATIVO")
        String status
) {
}
