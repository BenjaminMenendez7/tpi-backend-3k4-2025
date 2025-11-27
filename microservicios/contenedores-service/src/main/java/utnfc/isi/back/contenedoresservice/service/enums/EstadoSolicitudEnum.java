package utnfc.isi.back.contenedoresservice.service.enums;

import java.util.Arrays;

public enum EstadoSolicitudEnum {
    BORRADOR(1L), PROGRAMADA(2L), EN_TRANSITO(3L), ENTREGADA(4L);

    private final Long id;

    EstadoSolicitudEnum(Long id) { this.id = id; }

    public Long getId() { return id; }

    public static EstadoSolicitudEnum fromId(Long id) {
        return Arrays.stream(values())
                .filter(e -> e.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ID de estado inválido: " + id));
    }
}
