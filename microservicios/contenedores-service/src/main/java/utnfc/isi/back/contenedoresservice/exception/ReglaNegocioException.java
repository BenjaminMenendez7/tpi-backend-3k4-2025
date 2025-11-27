package utnfc.isi.back.contenedoresservice.exception;

/**
 * Excepción para reglas de negocio del TP.
 * Permite identificar el número y nombre de la regla en el mensaje.
 */
public class ReglaNegocioException extends RuntimeException {
    private final String codigo;

    // Constructor con un solo parámetro (mensaje)
    public ReglaNegocioException(String message) {
        super(message);
        this.codigo = "REGLA_NEGOCIO"; // Código por defecto
    }

    // Constructor modificado con dos parámetros (mensaje y código)
    public ReglaNegocioException(String codigo, String message) {
        super(String.format("[%s]: %s", codigo, message));
        this.codigo = codigo; // Asignar el código
    }

    public String getCodigo() {
        return codigo;
    }
}
