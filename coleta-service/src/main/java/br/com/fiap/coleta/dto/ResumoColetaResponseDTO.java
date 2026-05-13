package br.com.fiap.coleta.dto;

public record ResumoColetaResponseDTO(
        Long totalColetas,
        Long totalAgendadas,
        Long totalEmAndamento,
        Long totalRealizadas,
        Long totalCanceladas,
        Double quantidadeTotalColetada
) {
}
