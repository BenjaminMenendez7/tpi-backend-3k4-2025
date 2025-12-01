package utnfc.isi.back.common.exceptions;

public class ReglaNegocioException extends RuntimeException {
    private final String codigo;

    public ReglaNegocioException(String codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
