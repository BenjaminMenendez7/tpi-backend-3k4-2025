package utnfc.isi.back.common.exceptions;

import java.util.List;

public class TransicionInvalidaException extends RuntimeException {

    private final String estadoActualNombre;
    private final String nuevoEstadoNombre;
    private final List<String> permitidos;

    public TransicionInvalidaException(String estadoActualNombre,
                                       String nuevoEstadoNombre,
                                       List<String> permitidos) {
        super(String.format("Transición inválida: %s → %s. Permitidos: %s",
                estadoActualNombre, nuevoEstadoNombre, permitidos));
        this.estadoActualNombre = estadoActualNombre;
        this.nuevoEstadoNombre = nuevoEstadoNombre;
        this.permitidos = permitidos;
    }

    public String getEstadoActualNombre() { return estadoActualNombre; }
    public String getNuevoEstadoNombre() { return nuevoEstadoNombre; }
    public List<String> getPermitidos() { return permitidos; }
}

