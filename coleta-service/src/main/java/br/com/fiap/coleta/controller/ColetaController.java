package br.com.fiap.coleta.controller;

import br.com.fiap.coleta.dto.ColetaRequestDTO;
import br.com.fiap.coleta.dto.ColetaResponseDTO;
import br.com.fiap.coleta.dto.ResumoColetaResponseDTO;
import br.com.fiap.coleta.service.ColetaService;
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
@RequestMapping("/api/coletas")
@RequiredArgsConstructor
public class ColetaController {

    private final ColetaService coletaService;

    @PostMapping
    public ResponseEntity<ColetaResponseDTO> criar(@Valid @RequestBody ColetaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coletaService.criar(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<ColetaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(coletaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColetaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(coletaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColetaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ColetaRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(coletaService.atualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        coletaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ponto-coleta/{idPontoColeta}")
    public ResponseEntity<List<ColetaResponseDTO>> buscarPorPontoColeta(@PathVariable Long idPontoColeta) {
        return ResponseEntity.ok(coletaService.buscarPorPontoColeta(idPontoColeta));
    }

    @GetMapping("/residuo/{idResiduo}")
    public ResponseEntity<List<ColetaResponseDTO>> buscarPorResiduo(@PathVariable Long idResiduo) {
        return ResponseEntity.ok(coletaService.buscarPorResiduo(idResiduo));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ColetaResponseDTO>> buscarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(coletaService.buscarPorStatus(status));
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoColetaResponseDTO> gerarResumo() {
        return ResponseEntity.ok(coletaService.gerarResumo());
    }
}
