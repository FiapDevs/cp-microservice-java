package br.com.fiap.coleta.dto;

import java.time.LocalDateTime;

public record ColetaResponseDTO(
        Long id,
        Long idPontoColeta,
        Long idResiduo,
        LocalDateTime dataAgendamento,
        LocalDateTime dataColeta,
        Double quantidadeColetada,
        String status,
        String observacao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}
