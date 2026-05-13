package br.com.fiap.coleta.service;

import br.com.fiap.coleta.dto.ColetaRequestDTO;
import br.com.fiap.coleta.dto.ColetaResponseDTO;
import br.com.fiap.coleta.dto.ResumoColetaResponseDTO;
import br.com.fiap.coleta.entity.Coleta;
import br.com.fiap.coleta.exception.BusinessException;
import br.com.fiap.coleta.exception.ResourceNotFoundException;
import br.com.fiap.coleta.repository.ColetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ColetaService {

    private static final String STATUS_AGENDADA = "AGENDADA";
    private static final String STATUS_EM_ANDAMENTO = "EM_ANDAMENTO";
    private static final String STATUS_REALIZADA = "REALIZADA";
    private static final String STATUS_CANCELADA = "CANCELADA";

    private final ColetaRepository coletaRepository;

    @Transactional
    public ColetaResponseDTO criar(ColetaRequestDTO requestDTO) {
        Coleta coleta = Coleta.builder()
                .idPontoColeta(validarIdObrigatorio(requestDTO.idPontoColeta(), "O ID do ponto de coleta e obrigatorio"))
                .idResiduo(validarIdObrigatorio(requestDTO.idResiduo(), "O ID do residuo e obrigatorio"))
                .dataAgendamento(requestDTO.dataAgendamento())
                .dataColeta(requestDTO.dataColeta())
                .quantidadeColetada(requestDTO.quantidadeColetada())
                .status(definirStatus(requestDTO.status()))
                .observacao(normalizarTextoOpcional(requestDTO.observacao()))
                .dataCriacao(LocalDateTime.now())
                .build();

        validarColeta(coleta);

        return toResponseDTO(coletaRepository.save(coleta));
    }

    @Transactional(readOnly = true)
    public List<ColetaResponseDTO> listarTodos() {
        return coletaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ColetaResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    @Transactional
    public ColetaResponseDTO atualizar(Long id, ColetaRequestDTO requestDTO) {
        Coleta coleta = buscarEntidadePorId(id);

        coleta.setIdPontoColeta(validarIdObrigatorio(requestDTO.idPontoColeta(), "O ID do ponto de coleta e obrigatorio"));
        coleta.setIdResiduo(validarIdObrigatorio(requestDTO.idResiduo(), "O ID do residuo e obrigatorio"));
        coleta.setDataAgendamento(requestDTO.dataAgendamento());
        coleta.setDataColeta(requestDTO.dataColeta());
        coleta.setQuantidadeColetada(requestDTO.quantidadeColetada());
        coleta.setStatus(definirStatus(requestDTO.status()));
        coleta.setObservacao(normalizarTextoOpcional(requestDTO.observacao()));
        coleta.setDataAtualizacao(LocalDateTime.now());

        validarColeta(coleta);

        return toResponseDTO(coletaRepository.save(coleta));
    }

    @Transactional
    public void deletar(Long id) {
        Coleta coleta = buscarEntidadePorId(id);
        coletaRepository.delete(coleta);
    }

    @Transactional(readOnly = true)
    public List<ColetaResponseDTO> buscarPorPontoColeta(Long idPontoColeta) {
        return coletaRepository.findByIdPontoColeta(idPontoColeta)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ColetaResponseDTO> buscarPorResiduo(Long idResiduo) {
        return coletaRepository.findByIdResiduo(idResiduo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ColetaResponseDTO> buscarPorStatus(String status) {
        String statusNormalizado = validarStatus(status);
        return coletaRepository.findByStatusIgnoreCase(statusNormalizado)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResumoColetaResponseDTO gerarResumo() {
        return new ResumoColetaResponseDTO(
                coletaRepository.count(),
                coletaRepository.countByStatusIgnoreCase(STATUS_AGENDADA),
                coletaRepository.countByStatusIgnoreCase(STATUS_EM_ANDAMENTO),
                coletaRepository.countByStatusIgnoreCase(STATUS_REALIZADA),
                coletaRepository.countByStatusIgnoreCase(STATUS_CANCELADA),
                coletaRepository.somarQuantidadeColetadaRealizada()
        );
    }

    private Coleta buscarEntidadePorId(Long id) {
        return coletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coleta nao encontrada"));
    }

    private Long validarIdObrigatorio(Long id, String mensagem) {
        if (id == null) {
            throw new BusinessException(mensagem);
        }

        return id;
    }

    private String definirStatus(String status) {
        if (status == null) {
            return STATUS_AGENDADA;
        }

        return validarStatus(status);
    }

    private String validarStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new BusinessException("O status e obrigatorio");
        }

        String statusNormalizado = status.trim().toUpperCase(Locale.ROOT);
        if (!STATUS_AGENDADA.equals(statusNormalizado)
                && !STATUS_EM_ANDAMENTO.equals(statusNormalizado)
                && !STATUS_REALIZADA.equals(statusNormalizado)
                && !STATUS_CANCELADA.equals(statusNormalizado)) {
            throw new BusinessException("O status deve ser AGENDADA, EM_ANDAMENTO, REALIZADA ou CANCELADA");
        }

        return statusNormalizado;
    }

    private void validarColeta(Coleta coleta) {
        if (STATUS_REALIZADA.equals(coleta.getStatus())) {
            if (coleta.getDataColeta() == null) {
                throw new BusinessException("Coletas realizadas devem possuir data de coleta");
            }

            if (coleta.getQuantidadeColetada() == null) {
                throw new BusinessException("Coletas realizadas devem possuir quantidade coletada");
            }
        }

        if (STATUS_CANCELADA.equals(coleta.getStatus()) && coleta.getDataColeta() != null) {
            throw new BusinessException("Coletas canceladas nao devem possuir data de coleta");
        }
    }

    private String normalizarTextoOpcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private ColetaResponseDTO toResponseDTO(Coleta coleta) {
        return new ColetaResponseDTO(
                coleta.getId(),
                coleta.getIdPontoColeta(),
                coleta.getIdResiduo(),
                coleta.getDataAgendamento(),
                coleta.getDataColeta(),
                coleta.getQuantidadeColetada(),
                coleta.getStatus(),
                coleta.getObservacao(),
                coleta.getDataCriacao(),
                coleta.getDataAtualizacao()
        );
    }
}
