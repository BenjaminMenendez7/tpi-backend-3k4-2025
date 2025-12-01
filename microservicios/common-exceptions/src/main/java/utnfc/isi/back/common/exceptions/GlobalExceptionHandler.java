package utnfc.isi.back.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange; // CLASE WEBFLUX CORRECTA
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseStatusExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejo de ReglaNegocioException
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleReglaNegocioException(
            ReglaNegocioException ex,
            ServerWebExchange exchange // USAMOS WEBFLUX
    ) {
        String path = exchange.getRequest().getURI().getPath();
        logger.error("Error de negocio: {} | código={}", ex.getMessage(), ex.getCodigo());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "error", "Regla de negocio incumplida",
                        "codigo", ex.getCodigo(),
                        "mensaje", ex.getMessage(),
                        "path", path
                ));
    }

    // Manejo de TransicionInvalidaException
    @ExceptionHandler(TransicionInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleTransicionInvalida(
            TransicionInvalidaException ex,
            ServerWebExchange exchange // USAMOS WEBFLUX
    ) {
        String path = exchange.getRequest().getURI().getPath();
        logger.error("Transición inválida: de {} a {}", ex.getEstadoActualNombre(), ex.getNuevoEstadoNombre());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "error", "Transición inválida",
                        "detalle", "No se puede pasar de " + ex.getEstadoActualNombre() + " a " + ex.getNuevoEstadoNombre(),
                        "permitidos", ex.getPermitidos(),
                        "path", path
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            ServerWebExchange exchange // USAMOS WEBFLUX
    ) {
        String path = exchange.getRequest().getURI().getPath();
        logger.error("Resource not found: {} | path={}", ex.getMessage(), path);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        Map.of(
                                "timestamp", Instant.now().toString(),
                                "status", HttpStatus.NOT_FOUND.value(),
                                "error", "Not found",
                                "message", ex.getMessage(),
                                "path", path
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            ServerWebExchange exchange // USAMOS WEBFLUX
    ) {
        String path = exchange.getRequest().getURI().getPath();
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Validación fallida",
                        "message", errors,
                        "path", path
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex,
            ServerWebExchange exchange // USAMOS WEBFLUX
    ) {
        String path = exchange.getRequest().getURI().getPath();
        logger.error("Error inesperado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Error interno",
                        "message", "Ocurrio un error inesperado",
                        "path", path
                )
        );
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.FORBIDDEN.value(),
                        "error", "Forbidden",
                        "message", "No tiene permisos para realizar esta acción",
                        "path", request.getRequestURI()
                )
        );
    }
}