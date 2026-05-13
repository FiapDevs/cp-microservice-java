package br.com.fiap.residuo.dto;

public record OrientacaoDescarteResponseDTO(
        String tipoResiduo,
        String destinacaoRecomendada,
        String observacao
) {
}
