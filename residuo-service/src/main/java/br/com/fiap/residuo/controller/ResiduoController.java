package br.com.fiap.residuo.controller;

import br.com.fiap.residuo.dto.OrientacaoDescarteResponseDTO;
import br.com.fiap.residuo.dto.ResiduoRequestDTO;
import br.com.fiap.residuo.dto.ResiduoResponseDTO;
import br.com.fiap.residuo.service.ResiduoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/residuos")
@RequiredArgsConstructor
public class ResiduoController {

    private final ResiduoService residuoService;

    @PostMapping
    public ResponseEntity<ResiduoResponseDTO> criar(@Valid @RequestBody ResiduoRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(residuoService.criar(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<ResiduoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(residuoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResiduoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(residuoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResiduoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResiduoRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(residuoService.atualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        residuoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ponto-coleta/{idPontoColeta}")
    public ResponseEntity<List<ResiduoResponseDTO>> buscarPorPontoColeta(@PathVariable Long idPontoColeta) {
        return ResponseEntity.ok(residuoService.buscarPorPontoColeta(idPontoColeta));
    }

    @GetMapping("/tipo/{tipoResiduo}")
    public ResponseEntity<List<ResiduoResponseDTO>> buscarPorTipoResiduo(@PathVariable String tipoResiduo) {
        return ResponseEntity.ok(residuoService.buscarPorTipoResiduo(tipoResiduo));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ResiduoResponseDTO>> buscarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(residuoService.buscarPorStatus(status));
    }

    @GetMapping("/orientacao-descarte/{tipoResiduo}")
    public ResponseEntity<OrientacaoDescarteResponseDTO> buscarOrientacaoDescarte(@PathVariable String tipoResiduo) {
        return ResponseEntity.ok(residuoService.buscarOrientacaoDescarte(tipoResiduo));
    }
}
