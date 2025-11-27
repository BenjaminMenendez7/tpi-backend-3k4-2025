package utnfc.isi.back.contenedoresservice.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " no encontrado: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
