package br.com.fiap.coleta.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponse> tratarRecursoNaoEncontrado(ResourceNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarErroValidacao(MethodArgumentNotValidException exception) {
        List<String> erros = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de validacao", erros);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponse> tratarRegraNegocio(BusinessException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroGenerico(Exception exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", null);
    }

    private ResponseEntity<ErroResponse> buildErrorResponse(HttpStatus status, String mensagem, List<String> erros) {
        ErroResponse erroResponse = new ErroResponse(
                status.value(),
                mensagem,
                erros,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(erroResponse);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErroResponse(
            int status,
            String mensagem,
            List<String> erros,
            LocalDateTime timestamp
    ) {
    }
}
