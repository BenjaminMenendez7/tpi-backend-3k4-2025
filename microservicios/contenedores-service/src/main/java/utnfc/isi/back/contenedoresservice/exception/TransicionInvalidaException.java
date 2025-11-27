package utnfc.isi.back.contenedoresservice.exception;

import java.util.List;


public class TransicionInvalidaException extends RuntimeException {

    private final String estadoActualNombre;
    private final String nuevoEstadoNombre;
    private final List<String> permitidos;

    public TransicionInvalidaException(String estadoActualNombre,
                                       String nuevoEstadoNombre,
                                       List<String> permitidos) {
        super("Transición inválida");
        this.estadoActualNombre = estadoActualNombre;
        this.nuevoEstadoNombre = nuevoEstadoNombre;
        this.permitidos = permitidos;
    }

    public String getEstadoActualNombre() { return estadoActualNombre; }
    public String getNuevoEstadoNombre() { return nuevoEstadoNombre; }
    public List<String> getPermitidos() { return permitidos; }
}
