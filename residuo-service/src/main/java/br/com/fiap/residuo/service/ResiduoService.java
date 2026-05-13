package br.com.fiap.residuo.service;

import br.com.fiap.residuo.dto.OrientacaoDescarteResponseDTO;
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
import java.util.Locale;

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
                .nome(normalizarTextoObrigatorio(requestDTO.nome(), "O nome do residuo e obrigatorio"))
                .tipoResiduo(normalizarTextoObrigatorio(requestDTO.tipoResiduo(), "O tipo do residuo e obrigatorio").toUpperCase(Locale.ROOT))
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
        residuo.setNome(normalizarTextoObrigatorio(requestDTO.nome(), "O nome do residuo e obrigatorio"));
        residuo.setTipoResiduo(normalizarTextoObrigatorio(requestDTO.tipoResiduo(), "O tipo do residuo e obrigatorio").toUpperCase(Locale.ROOT));
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

    @Transactional(readOnly = true)
    public OrientacaoDescarteResponseDTO buscarOrientacaoDescarte(String tipoResiduo) {
        String tipoNormalizado = normalizarTextoObrigatorio(tipoResiduo, "O tipo do residuo e obrigatorio")
                .toUpperCase(Locale.ROOT);

        return switch (tipoNormalizado) {
            case "PAPEL" -> new OrientacaoDescarteResponseDTO(
                    tipoNormalizado,
                    "Coleta seletiva de papel e papelao",
                    "Evite descartar papel sujo com restos de alimento."
            );
            case "PLASTICO" -> new OrientacaoDescarteResponseDTO(
                    tipoNormalizado,
                    "Coleta seletiva de plasticos reciclaveis",
                    "Sempre que possivel, lave embalagens antes do descarte."
            );
            case "VIDRO" -> new OrientacaoDescarteResponseDTO(
                    tipoNormalizado,
                    "Ponto de coleta para vidro",
                    "Embale vidros quebrados para evitar acidentes."
            );
            case "METAL" -> new OrientacaoDescarteResponseDTO(
                    tipoNormalizado,
                    "Coleta seletiva de metais",
                    "Latas e pecas metalicas devem ser separadas dos residuos organicos."
            );
            case "ORGANICO" -> new OrientacaoDescarteResponseDTO(
                    tipoNormalizado,
                    "Compostagem ou coleta de residuos organicos",
                    "Separe restos de alimentos de embalagens reciclaveis."
            );
            case "ELETRONICO" -> new OrientacaoDescarteResponseDTO(
                    tipoNormalizado,
                    "Ponto de coleta especifico para lixo eletronico",
                    "Nao descarte baterias, pilhas ou equipamentos eletronicos no lixo comum."
            );
            default -> new OrientacaoDescarteResponseDTO(
                    tipoNormalizado,
                    "Verificar ponto de coleta adequado",
                    "Consulte a prefeitura ou cooperativas locais para destinacao correta."
            );
        };
    }

    private Residuo buscarEntidadePorId(Long id) {
        return residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Residuo nao encontrado"));
    }

    private Long validarIdPontoColeta(Long idPontoColeta) {
        if (idPontoColeta == null) {
            throw new BusinessException("O ID do ponto de coleta e obrigatorio");
        }

        return idPontoColeta;
    }

    private Double validarQuantidade(Double quantidade) {
        if (quantidade == null) {
            throw new BusinessException("A quantidade e obrigatoria");
        }

        if (quantidade < 0) {
            throw new BusinessException("A quantidade nao pode ser negativa");
        }

        return quantidade;
    }

    private String definirUnidadeMedida(String unidadeMedida) {
        if (unidadeMedida == null) {
            return UNIDADE_MEDIDA_PADRAO;
        }

        if (unidadeMedida.isBlank()) {
            throw new BusinessException("A unidade de medida nao pode estar em branco");
        }

        return unidadeMedida.trim().toUpperCase(Locale.ROOT);
    }

    private String definirStatus(String status) {
        if (status == null) {
            return STATUS_ATIVO;
        }

        return validarStatus(status);
    }

    private String validarStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new BusinessException("O status e obrigatorio");
        }

        String statusNormalizado = status.trim().toUpperCase(Locale.ROOT);
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
