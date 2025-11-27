package utnfc.isi.back.contenedoresservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejo de ReglaNegocioException
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleReglaNegocioException(ReglaNegocioException ex) {
        // Log de la excepción
        logger.error("Error de negocio: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "codigo", "REGLA_NEGOCIO_1",
                        "mensaje", ex.getMessage()
                ));
    }

    // Manejo de TransicionInvalidaException
    @ExceptionHandler(TransicionInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleTransicionInvalida(TransicionInvalidaException ex) {

        // Log de la excepción
        logger.error("Transición inválida: de {} a {}", ex.getEstadoActualNombre(), ex.getNuevoEstadoNombre());

        return ResponseEntity.badRequest().body(
                Map.of(
                        "error", "Transición inválida",
                        "detalle", "No se puede pasar de " +
                                ex.getEstadoActualNombre() +
                                " a " +
                                ex.getNuevoEstadoNombre(),
                        "permitidos", ex.getPermitidos()
                )
        );
    }
}

