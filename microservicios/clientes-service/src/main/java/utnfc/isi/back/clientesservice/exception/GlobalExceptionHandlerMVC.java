package utnfc.isi.back.clientesservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerMVC {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();
        String message = "Violación de restricción de integridad";

        // PostgreSQL incluye el nombre de la constraint en el mensaje
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("cliente_documento_key")) {
                message = "Ya existe un cliente con ese documento";
            } else if (ex.getMessage().contains("cliente_email_key")) {
                message = "Ya existe un cliente con ese email";
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.CONFLICT.value(),
                        "error", "Conflict",
                        "message", message

                )
        );
    }
}
