package br.com.fiap.residuo.dto;

import java.time.LocalDateTime;

public record ResiduoResponseDTO(
        Long id,
        Long idPontoColeta,
        String nome,
        String tipoResiduo,
        String descricao,
        Double quantidade,
        String unidadeMedida,
        String status,
        LocalDateTime dataRegistro,
        LocalDateTime dataAtualizacao
) {
}
