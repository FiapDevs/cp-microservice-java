package br.com.fiap.residuo.service;

import br.com.fiap.residuo.dto.ResiduoRequestDTO;
import br.com.fiap.residuo.dto.ResiduoResponseDTO;
import br.com.fiap.residuo.entity.Residuo;
import br.com.fiap.residuo.exception.BusinessException;
import br.com.fiap.residuo.exception.ResourceNotFoundException;
import br.com.fiap.residuo.repository.ResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResiduoService {

    private static final String STATUS_ATIVO = "ATIVO";
    private static final String STATUS_INATIVO = "INATIVO";
    private static final String UNIDADE_MEDIDA_PADRAO = "KG";

    private final ResiduoRepository residuoRepository;

    @Transactional
    public ResiduoResponseDTO criar(ResiduoRequestDTO requestDTO) {
        Residuo residuo = Residuo.builder()
                .idPontoColeta(validarIdPontoColeta(requestDTO.idPontoColeta()))
                .nome(normalizarTextoObrigatorio(requestDTO.nome(), "O nome do resíduo é obrigatório"))
                .tipoResiduo(normalizarTextoObrigatorio(requestDTO.tipoResiduo(), "O tipo do resíduo é obrigatório"))
                .descricao(normalizarTextoOpcional(requestDTO.descricao()))
                .quantidade(validarQuantidade(requestDTO.quantidade()))
                .unidadeMedida(definirUnidadeMedida(requestDTO.unidadeMedida()))
                .status(definirStatus(requestDTO.status()))
                .dataRegistro(LocalDateTime.now())
                .build();

        return toResponseDTO(residuoRepository.save(residuo));
    }

    @Transactional(readOnly = true)
    public List<ResiduoResponseDTO> listarTodos() {
        return residuoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResiduoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    @Transactional
    public ResiduoResponseDTO atualizar(Long id, ResiduoRequestDTO requestDTO) {
        Residuo residuo = buscarEntidadePorId(id);

        residuo.setIdPontoColeta(validarIdPontoColeta(requestDTO.idPontoColeta()));
        residuo.setNome(normalizarTextoObrigatorio(requestDTO.nome(), "O nome do resíduo é obrigatório"));
        residuo.setTipoResiduo(normalizarTextoObrigatorio(requestDTO.tipoResiduo(), "O tipo do resíduo é obrigatório"));
        residuo.setDescricao(normalizarTextoOpcional(requestDTO.descricao()));
        residuo.setQuantidade(validarQuantidade(requestDTO.quantidade()));
        residuo.setUnidadeMedida(definirUnidadeMedida(requestDTO.unidadeMedida()));
        residuo.setStatus(definirStatus(requestDTO.status()));
        residuo.setDataAtualizacao(LocalDateTime.now());

        return toResponseDTO(residuoRepository.save(residuo));
    }

    @Transactional
    public void deletar(Long id) {
        Residuo residuo = buscarEntidadePorId(id);
        residuoRepository.delete(residuo);
    }

    @Transactional(readOnly = true)
    public List<ResiduoResponseDTO> buscarPorPontoColeta(Long idPontoColeta) {
        return residuoRepository.findByIdPontoColeta(idPontoColeta)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResiduoResponseDTO> buscarPorTipoResiduo(String tipoResiduo) {
        return residuoRepository.findByTipoResiduoIgnoreCase(tipoResiduo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResiduoResponseDTO> buscarPorStatus(String status) {
        String statusNormalizado = validarStatus(status);
        return residuoRepository.findByStatusIgnoreCase(statusNormalizado)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private Residuo buscarEntidadePorId(Long id) {
        return residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resíduo não encontrado"));
    }

    private Long validarIdPontoColeta(Long idPontoColeta) {
        if (idPontoColeta == null) {
            throw new BusinessException("O ID do ponto de coleta é obrigatório");
        }

        return idPontoColeta;
    }

    private Double validarQuantidade(Double quantidade) {
        if (quantidade == null) {
            throw new BusinessException("A quantidade é obrigatória");
        }

        if (quantidade < 0) {
            throw new BusinessException("A quantidade não pode ser negativa");
        }

        return quantidade;
    }

    private String definirUnidadeMedida(String unidadeMedida) {
        if (unidadeMedida == null) {
            return UNIDADE_MEDIDA_PADRAO;
        }

        if (unidadeMedida.isBlank()) {
            throw new BusinessException("A unidade de medida não pode estar em branco");
        }

        return unidadeMedida.trim().toUpperCase();
    }

    private String definirStatus(String status) {
        if (status == null) {
            return STATUS_ATIVO;
        }

        return validarStatus(status);
    }

    private String validarStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new BusinessException("O status é obrigatório");
        }

        String statusNormalizado = status.trim().toUpperCase();
        if (!STATUS_ATIVO.equals(statusNormalizado) && !STATUS_INATIVO.equals(statusNormalizado)) {
            throw new BusinessException("O status deve ser ATIVO ou INATIVO");
        }

        return statusNormalizado;
    }

    private String normalizarTextoObrigatorio(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new BusinessException(mensagem);
        }

        return texto.trim();
    }

    private String normalizarTextoOpcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private ResiduoResponseDTO toResponseDTO(Residuo residuo) {
        return new ResiduoResponseDTO(
                residuo.getId(),
                residuo.getIdPontoColeta(),
                residuo.getNome(),
                residuo.getTipoResiduo(),
                residuo.getDescricao(),
                residuo.getQuantidade(),
                residuo.getUnidadeMedida(),
                residuo.getStatus(),
                residuo.getDataRegistro(),
                residuo.getDataAtualizacao()
        );
    }
}
