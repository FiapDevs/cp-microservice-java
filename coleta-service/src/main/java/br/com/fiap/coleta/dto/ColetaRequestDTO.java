package br.com.fiap.coleta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ColetaRequestDTO(
        @NotNull(message = "O ID do ponto de coleta e obrigatorio")
        Long idPontoColeta,

        @NotNull(message = "O ID do residuo e obrigatorio")
        Long idResiduo,

        @NotNull(message = "A data de agendamento e obrigatoria")
        @FutureOrPresent(message = "A data de agendamento nao pode estar no passado")
        LocalDateTime dataAgendamento,

        LocalDateTime dataColeta,

        @DecimalMin(value = "0.0", inclusive = true, message = "A quantidade coletada nao pode ser negativa")
        Double quantidadeColetada,

        @Pattern(regexp = "(?i:AGENDADA|EM_ANDAMENTO|REALIZADA|CANCELADA)", message = "O status deve ser AGENDADA, EM_ANDAMENTO, REALIZADA ou CANCELADA")
        String status,

        @Size(max = 250, message = "A observacao deve ter no maximo 250 caracteres")
        String observacao
) {
}
