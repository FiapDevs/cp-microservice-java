package br.com.fiap.pontocoleta.service;

import br.com.fiap.pontocoleta.dto.AlertaCapacidadeResponseDTO;
import br.com.fiap.pontocoleta.dto.PontoColetaRequestDTO;
import br.com.fiap.pontocoleta.dto.PontoColetaResponseDTO;
import br.com.fiap.pontocoleta.entity.PontoColeta;
import br.com.fiap.pontocoleta.exception.BusinessException;
import br.com.fiap.pontocoleta.exception.ResourceNotFoundException;
import br.com.fiap.pontocoleta.repository.PontoColetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PontoColetaService {

    private static final String STATUS_ATIVO = "ATIVO";
    private static final String STATUS_INATIVO = "INATIVO";
    private static final String STATUS_LOTADO = "LOTADO";

    private final PontoColetaRepository repository;

    @Transactional
    public PontoColetaResponseDTO criar(PontoColetaRequestDTO requestDTO) {
        validarCapacidades(requestDTO.getCapacidadeAtual(), requestDTO.getCapacidadeMaxima());

        PontoColeta pontoColeta = toEntity(requestDTO);
        pontoColeta.setStatus(definirStatus(requestDTO.getStatus(), requestDTO.getCapacidadeAtual(), requestDTO.getCapacidadeMaxima()));
        pontoColeta.setDataCriacao(LocalDateTime.now());

        return toResponseDTO(repository.save(pontoColeta));
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PontoColetaResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    @Transactional
    public PontoColetaResponseDTO atualizar(Long id, PontoColetaRequestDTO requestDTO) {
        validarCapacidades(requestDTO.getCapacidadeAtual(), requestDTO.getCapacidadeMaxima());

        PontoColeta pontoColeta = buscarEntidadePorId(id);
        preencherDados(pontoColeta, requestDTO);
        pontoColeta.setStatus(definirStatus(requestDTO.getStatus(), requestDTO.getCapacidadeAtual(), requestDTO.getCapacidadeMaxima()));
        pontoColeta.setDataAtualizacao(LocalDateTime.now());

        return toResponseDTO(repository.save(pontoColeta));
    }

    @Transactional
    public void deletar(Long id) {
        PontoColeta pontoColeta = buscarEntidadePorId(id);
        repository.delete(pontoColeta);
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> buscarPorCidade(String cidade) {
        return repository.findByCidadeIgnoreCase(cidade).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> buscarPorStatus(String status) {
        validarStatus(status);
        return repository.findByStatusIgnoreCase(normalizar(status)).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> buscarPorTipoResiduo(String tipoResiduo) {
        return repository.findByTipoResiduoAceitoIgnoreCase(tipoResiduo).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlertaCapacidadeResponseDTO> listarAlertasCapacidade(Double percentualMinimo) {
        double limite = percentualMinimo == null ? 80.0 : percentualMinimo;

        if (limite < 0 || limite > 100) {
            throw new BusinessException("O percentual minimo deve estar entre 0 e 100");
        }

        return repository.findComCapacidadeAcimaDoLimite(limite).stream()
                .map(this::toAlertaCapacidadeResponseDTO)
                .toList();
    }

    private PontoColeta buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de coleta nao encontrado"));
    }

    private PontoColeta toEntity(PontoColetaRequestDTO requestDTO) {
        PontoColeta pontoColeta = new PontoColeta();
        preencherDados(pontoColeta, requestDTO);
        return pontoColeta;
    }

    private void preencherDados(PontoColeta pontoColeta, PontoColetaRequestDTO requestDTO) {
        pontoColeta.setNome(requestDTO.getNome());
        pontoColeta.setEndereco(requestDTO.getEndereco());
        pontoColeta.setCidade(requestDTO.getCidade());
        pontoColeta.setEstado(normalizar(requestDTO.getEstado()));
        pontoColeta.setCep(requestDTO.getCep());
        pontoColeta.setLatitude(requestDTO.getLatitude());
        pontoColeta.setLongitude(requestDTO.getLongitude());
        pontoColeta.setCapacidadeMaxima(requestDTO.getCapacidadeMaxima());
        pontoColeta.setCapacidadeAtual(requestDTO.getCapacidadeAtual());
        pontoColeta.setTipoResiduoAceito(requestDTO.getTipoResiduoAceito());
    }

    private PontoColetaResponseDTO toResponseDTO(PontoColeta pontoColeta) {
        return PontoColetaResponseDTO.builder()
                .id(pontoColeta.getId())
                .nome(pontoColeta.getNome())
                .endereco(pontoColeta.getEndereco())
                .cidade(pontoColeta.getCidade())
                .estado(pontoColeta.getEstado())
                .cep(pontoColeta.getCep())
                .latitude(pontoColeta.getLatitude())
                .longitude(pontoColeta.getLongitude())
                .capacidadeMaxima(pontoColeta.getCapacidadeMaxima())
                .capacidadeAtual(pontoColeta.getCapacidadeAtual())
                .tipoResiduoAceito(pontoColeta.getTipoResiduoAceito())
                .status(pontoColeta.getStatus())
                .dataCriacao(pontoColeta.getDataCriacao())
                .dataAtualizacao(pontoColeta.getDataAtualizacao())
                .build();
    }

    private AlertaCapacidadeResponseDTO toAlertaCapacidadeResponseDTO(PontoColeta pontoColeta) {
        double percentualOcupacao = calcularPercentualOcupacao(pontoColeta);

        return AlertaCapacidadeResponseDTO.builder()
                .id(pontoColeta.getId())
                .nome(pontoColeta.getNome())
                .cidade(pontoColeta.getCidade())
                .estado(pontoColeta.getEstado())
                .capacidadeMaxima(pontoColeta.getCapacidadeMaxima())
                .capacidadeAtual(pontoColeta.getCapacidadeAtual())
                .percentualOcupacao(percentualOcupacao)
                .status(pontoColeta.getStatus())
                .mensagem(criarMensagemAlerta(percentualOcupacao))
                .build();
    }

    private double calcularPercentualOcupacao(PontoColeta pontoColeta) {
        return (pontoColeta.getCapacidadeAtual() / pontoColeta.getCapacidadeMaxima()) * 100;
    }

    private String criarMensagemAlerta(double percentualOcupacao) {
        if (percentualOcupacao >= 100) {
            return "Ponto de coleta lotado. Acionar coleta imediatamente.";
        }

        return "Ponto de coleta proximo do limite. Recomenda-se agendar coleta.";
    }

    private void validarCapacidades(Double capacidadeAtual, Double capacidadeMaxima) {
        if (capacidadeAtual > capacidadeMaxima) {
            throw new BusinessException("A capacidade atual nao pode ser maior que a capacidade maxima");
        }
    }

    private String definirStatus(String statusInformado, Double capacidadeAtual, Double capacidadeMaxima) {
        if (capacidadeAtual >= capacidadeMaxima) {
            return STATUS_LOTADO;
        }

        if (statusInformado == null || statusInformado.isBlank()) {
            return STATUS_ATIVO;
        }

        String statusNormalizado = normalizar(statusInformado);
        validarStatus(statusNormalizado);
        return statusNormalizado;
    }

    private void validarStatus(String status) {
        String statusNormalizado = normalizar(status);
        if (!STATUS_ATIVO.equals(statusNormalizado)
                && !STATUS_INATIVO.equals(statusNormalizado)
                && !STATUS_LOTADO.equals(statusNormalizado)) {
            throw new BusinessException("O status deve ser ATIVO, INATIVO ou LOTADO");
        }
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim().toUpperCase(Locale.ROOT);
    }
}
