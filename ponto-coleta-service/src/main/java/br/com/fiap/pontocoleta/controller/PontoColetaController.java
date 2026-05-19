package br.com.fiap.pontocoleta.controller;

import br.com.fiap.pontocoleta.dto.AlertaCapacidadeResponseDTO;
import br.com.fiap.pontocoleta.dto.PontoColetaRequestDTO;
import br.com.fiap.pontocoleta.dto.PontoColetaResponseDTO;
import br.com.fiap.pontocoleta.service.PontoColetaService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pontos-coleta")
@RequiredArgsConstructor
public class PontoColetaController {

    private final PontoColetaService service;

    @PostMapping
    public ResponseEntity<PontoColetaResponseDTO> criar(@Valid @RequestBody PontoColetaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PontoColetaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PontoColetaRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<PontoColetaResponseDTO>> buscarPorCidade(@PathVariable String cidade) {
        return ResponseEntity.ok(service.buscarPorCidade(cidade));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PontoColetaResponseDTO>> buscarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.buscarPorStatus(status));
    }

    @GetMapping("/tipo-residuo/{tipoResiduo}")
    public ResponseEntity<List<PontoColetaResponseDTO>> buscarPorTipoResiduo(@PathVariable String tipoResiduo) {
        return ResponseEntity.ok(service.buscarPorTipoResiduo(tipoResiduo));
    }

    @GetMapping("/alertas-capacidade")
    public ResponseEntity<List<AlertaCapacidadeResponseDTO>> listarAlertasCapacidade(
            @RequestParam(required = false) Double percentualMinimo
    ) {
        return ResponseEntity.ok(service.listarAlertasCapacidade(percentualMinimo));
    }
}
