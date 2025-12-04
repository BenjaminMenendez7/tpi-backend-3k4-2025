package utnfc.isi.back.contenedoresservice.service.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EstadoSolicitudEnum {
    BORRADOR(1L), PROGRAMADA(2L), EN_TRANSITO(3L), ENTREGADA(4L);

    private final Long id;

    EstadoSolicitudEnum(Long id) { this.id = id; }

    public static EstadoSolicitudEnum fromId(Long id) {
        return Arrays.stream(values())
                .filter(e -> e.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ID de estado inválido: " + id));
    }
}
